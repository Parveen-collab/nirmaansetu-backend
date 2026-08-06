package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.entity.RefreshToken;
import com.nirmaansetu.backend.modules.auth.repository.RefreshTokenRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Creates and stores a refresh token for a user.
     */
    public String createRefreshToken(User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates a refresh token.
     */
    public boolean validateRefreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Rotates a refresh token.
     */
    public String rotateRefreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Revokes one refresh token.
     */
    public void revokeRefreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Revokes every refresh token of a user.
     */
    public void revokeAllTokens(User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Deletes expired refresh tokens.
     */
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }

    /**
     * Hash refresh token before storing.
     */
    private String hashToken(String token) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(token.getBytes());

            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Unable to hash refresh token", e);

        }
    }
}