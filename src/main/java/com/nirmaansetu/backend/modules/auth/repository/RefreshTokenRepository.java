package com.nirmaansetu.backend.modules.auth.repository;

import com.nirmaansetu.backend.modules.auth.entity.RefreshToken;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by its hashed value.
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Get all active refresh tokens of a user.
     */
    List<RefreshToken> findByUserAndRevokedFalse(User user);

    /**
     * Delete all expired refresh tokens.
     */
    void deleteByExpiresAtBefore(Instant now);

    /**
     * Revoke all refresh tokens belonging to a user.
     */
    List<RefreshToken> findByUser(User user);
}