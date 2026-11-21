package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.dto.JwtUserDto;
import com.nirmaansetu.backend.modules.auth.entity.RefreshToken;
import com.nirmaansetu.backend.modules.auth.mapper.JwtMapper;
import com.nirmaansetu.backend.modules.auth.repository.RefreshTokenRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtMapper jwtMapper;

    @Value("${jwt.expiration.refresh:604800000}")
    private long refreshExpirationMs;

    /**
     * Creates, persists, and returns a new refresh token for a user.
     * The token is valid for the configured refresh window (e.g. 7 days).
     */
    public String createRefreshToken(User user) {
        JwtUserDto jwtUser = jwtMapper.toJwtUser(user);
        String rawToken = jwtUtil.generateToken(jwtUser, true);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(hashToken(rawToken));
        entity.setExpiresAt(Instant.now().plusMillis(refreshExpirationMs));
        entity.setRevoked(false);
        entity.setLastUsedAt(Instant.now());

        refreshTokenRepository.save(entity);
        return rawToken;
    }

    /**
     * Validates a refresh token by checking both the persisted DB state and JWT cryptographic validity.
     */
    public boolean validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return false;
        }

        String hash = hashToken(refreshToken);
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByTokenHash(hash);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        RefreshToken entity = tokenOpt.get();
        if (entity.isRevoked() || entity.getExpiresAt().isBefore(Instant.now())) {
            return false;
        }

        String phone = jwtUtil.extractPhoneNumber(refreshToken);
        return phone != null && jwtUtil.validateToken(refreshToken, phone);
    }

    /**
     * Finds a RefreshToken entity by its raw token string.
     */
    public Optional<RefreshToken> findByToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return Optional.empty();
        }
        return refreshTokenRepository.findByTokenHash(hashToken(refreshToken));
    }

    /**
     * Rotates a refresh token:
     * - Validates the old token against the database and time window.
     * - If expired or revoked, throws BadCredentialsException (Case 2: Redirect to login).
     * - Revokes the old token, issues a new token, and records usage (Case 1: Proceed to dashboard).
     */
    public String rotateRefreshToken(String oldRefreshToken) {
        if (oldRefreshToken == null || oldRefreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }

        String oldHash = hashToken(oldRefreshToken);
        Optional<RefreshToken> oldEntityOpt = refreshTokenRepository.findByTokenHash(oldHash);

        User user;
        if (oldEntityOpt.isPresent()) {
            RefreshToken oldEntity = oldEntityOpt.get();

            if (oldEntity.isRevoked()) {
                throw new BadCredentialsException("Session has been revoked. Please log in again.");
            }

            if (oldEntity.getExpiresAt().isBefore(Instant.now())) {
                throw new BadCredentialsException("Session has expired. Please log in again.");
            }

            // Revoke old token for rotation security
            oldEntity.setRevoked(true);
            oldEntity.setLastUsedAt(Instant.now());
            refreshTokenRepository.save(oldEntity);

            user = oldEntity.getUser();
        } else {
            // Handle transitional token if signed by JWT secret but not yet in DB
            String phone;
            try {
                phone = jwtUtil.extractPhoneNumber(oldRefreshToken);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid or expired session. Please log in again.");
            }

            if (phone == null || !jwtUtil.validateToken(oldRefreshToken, phone)) {
                throw new BadCredentialsException("Session has expired. Please log in again.");
            }
            throw new BadCredentialsException("Session not found. Please log in again.");
        }

        // Issue new refresh token within the renewed time window
        return createRefreshToken(user);
    }

    /**
     * Revokes one refresh token (e.g. on user logout).
     */
    public void revokeRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        refreshTokenRepository.findByTokenHash(hashToken(refreshToken)).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    /**
     * Revokes all refresh tokens belonging to a user (e.g. on password change or remote logout).
     */
    public void revokeAllTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedFalse(user);
        for (RefreshToken token : tokens) {
            token.setRevoked(true);
        }
        refreshTokenRepository.saveAll(tokens);
    }

    /**
     * Deletes expired refresh tokens from the database.
     * Scheduled to run daily at 3:00 AM.
     */
    @Scheduled(cron = "${app.cleanup.refresh-tokens.cron:0 0 3 * * *}")
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }

    /**
     * Hash refresh token before storing using SHA-256.
     */
    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to hash refresh token", e);
        }
    }
}
