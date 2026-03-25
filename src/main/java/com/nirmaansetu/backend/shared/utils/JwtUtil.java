package com.nirmaansetu.backend.shared.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "your-very-secure-secret-key-that-is-at-least-256-bits-long";
    private final long accessExpiration = 3600000; // 1 hour
    private final long refreshExpiration = 604800000; // 7 days

    public String generateToken(String phoneNumber, boolean isRefresh) {
        long expiry = isRefresh ? refreshExpiration : accessExpiration;
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
