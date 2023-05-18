package com.example.nikPay.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private final SecretKey secretKey;
    private final SignatureAlgorithm algorithm;

    public JwtUtil(SecretKey secretKey, SignatureAlgorithm algorithm) {
        this.secretKey = secretKey;
        this.algorithm = algorithm;
    }

    public String generateToken(String subject, long expirationMillis) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(this.secretKey, this.algorithm);

        return builder.compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean verifyToken(String token) {
        try {
            // Parse the token and retrieve the claims
            Claims claims = parseToken(token);

            // Check if the token has expired
            Date expiration = claims.getExpiration();
            Date now = new Date();
            if (expiration.before(now)) {
                // Token has expired
                return false;
            }

            return true;
        } catch (Exception e) {
            // Invalid token
            return false;
        }
    }

}
