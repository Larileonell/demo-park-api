package com.allanaoliveira.demo_park_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Slf4j
public class JwtUtils {

    private static final String JWT_BEARER = "Bearer ";
    private static final String JWT_AUTHORIZATION = "Authorization";
    private static final String SECRET_KEY = "0123456789-0123456789-0123456789";

    private static final long EXPIRE_DAYS = 0;
    private static final long EXPIRE_HOURS = 0;
    private static final long EXPIRE_MINUTES = 30;

    public JwtUtils() {
    }


    private static Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    }

    private static Date toExiperDate(Date date) {
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createJwtToken(String username, String role) {
        Date issuedAt = new Date();
        Date limit = toExiperDate(issuedAt);
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException ex) {
            log.error(String.format("Token '%s' is invalid", ex.getMessage()));
        }
        return null;
    }


    public static String getUsernameFromToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }



    private static String refactorToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        if (token.startsWith(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }

        return token;
    }

    public static boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(refactorToken(token));

            return true;
        } catch (JwtException ex) {
            log.error("Token inválido: {}", ex.getMessage());
            return false;
        }
    }



}