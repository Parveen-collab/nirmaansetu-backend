package com.nirmaansetu.backend.modules.auth.entity;

import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.shared.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Owner of this refresh token
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * SHA-256 hash of refresh token
     */
    @Column(nullable = false, unique = true, length = 128)
    private String tokenHash;

    /**
     * Expiration time
     */
    @Column(nullable = false)
    private Instant expiresAt;

    /**
     * Token revoked?
     */
    @Column(nullable = false)
    private boolean revoked = false;

    /**
     * Last time this refresh token was used
     */
    private Instant lastUsedAt;

    /**
     * Optional device name
     */
    private String deviceName;

    /**
     * Optional IP Address
     */
    private String ipAddress;
}
