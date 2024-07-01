package com.innov8ors.backend.security.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private static final String secretKeyStr = "Innv8ors-back-end-secret-key";

    private static byte[] secretKey = secretKeyStr.getBytes(StandardCharsets.UTF_8);

    private Long tokenValidityInMilliseconds = 300000L; // Token expiration time in milliseconds

    public String generateToken(UserDetails userDetails) {
        // Create claims (user details, expiration time, etc.)
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + tokenValidityInMilliseconds;
        Date exp = new Date(expMillis);

        // Create JWT using the claims and signing key
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString()) // Unique token ID
                .setSubject(userDetails.getUsername()) // Subject of the token
                .setIssuedAt(now)
                .setExpiration(exp)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey);

        return builder.compact();
    }

    public Jws<Claims> validateToken(String token) {
        try {
            // Parse the token and verify its signature
            Jws<Claims> jwt = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return jwt;
        } catch (IllegalArgumentException e) {
            // Invalid arguments provided to parser
            return null;
        }
    }

    public static String getEmailFromToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            
            return claims.get("username", String.class);
        } catch (JwtException ex) {
            // If Error
            System.out.println("Token recebido: ");
            System.out.println(jwtToken);
            ex.printStackTrace();
            return null;
        }
    }
}

