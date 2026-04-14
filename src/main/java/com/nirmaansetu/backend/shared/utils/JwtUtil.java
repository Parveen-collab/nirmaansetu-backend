package com.nirmaansetu.backend.shared.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//This class is the core JWT utility 🔐 of the backend — it handles:
//Token generation
//Token parsing
//Token validation
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access}")
    private long accessExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String phoneNumber, boolean isRefresh) {
        long expiry = isRefresh ? refreshExpiration : accessExpiration;
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String phoneNumber) {
        final String extractedPhoneNumber = extractPhoneNumber(token);
        return (extractedPhoneNumber.equals(phoneNumber) && !isTokenExpired(token));
    }
}
