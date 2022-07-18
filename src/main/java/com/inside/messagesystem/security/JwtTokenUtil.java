package com.inside.messagesystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.token.access}")
    private String accessSecretWord;

    @Value("${jwt.token.access.expiration}")
    private int accessTokenExpirationTime;

    public String generateAccessToken(Authentication authentication) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expiryDate = now.plus(Duration.ofMinutes(accessTokenExpirationTime));

        long userId = user.getId();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("name", user.getUsername());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(claimsMap)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, accessSecretWord)
                .compact();
    }

    public boolean accessTokenIsValid(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(accessSecretWord)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(accessSecretWord)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("name");
    }
}
