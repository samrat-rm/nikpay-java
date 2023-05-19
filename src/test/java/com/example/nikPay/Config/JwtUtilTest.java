package com.example.nikPay.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private SecretKey secretKey;

    @BeforeEach
    public void setup() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtUtil = new JwtUtil(secretKey, SignatureAlgorithm.HS256);
    }

    @Test
    public void testGenerateToken() {
        // Arrange
        String subject = "user123";
        long expirationMillis = 10000; // 10 seconds

        // Act
        String token = jwtUtil.generateToken(subject, expirationMillis);

        // Assert
        Assertions.assertNotNull(token);

        Claims claims = jwtUtil.parseToken(token);
        String parsedSubject = claims.getSubject();
        Assertions.assertEquals(subject, parsedSubject);

        Date expirationDate = claims.getExpiration();
        Assertions.assertNotNull(expirationDate);
    }


    @Test
    public void testParseToken_ValidToken() {
        // Arrange
        String subject = "user123";
        long expirationMillis = 10000; // 10 seconds

        String token = jwtUtil.generateToken(subject, expirationMillis);

        // Act
        Claims claims = jwtUtil.parseToken(token);

        // Assert
        Assertions.assertNotNull(claims);
        Assertions.assertEquals(subject, claims.getSubject());
    }

    @Test
    public void testParseToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";

        // Act & Assert
      Assertions.assertThrows(io.jsonwebtoken.MalformedJwtException.class,
                () -> jwtUtil.parseToken(invalidToken));
    }

    @Test
    public void testVerifyToken_ValidToken() {
        // Arrange
        String subject = "user123";
        long expirationMillis = 10000; // 10 seconds

        String token = jwtUtil.generateToken(subject, expirationMillis);

        // Act
        boolean isValid = jwtUtil.verifyToken(token);

        // Assert
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testVerifyToken_ExpiredToken() throws InterruptedException {
        // Arrange
        String subject = "user123";
        long expirationMillis = 1000; // 1 second

        String token = jwtUtil.generateToken(subject, expirationMillis);

        // Wait for token to expire
        Thread.sleep(2000);

        // Act
        boolean isValid = jwtUtil.verifyToken(token);

        // Assert
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testVerifyToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";

        // Act
        boolean isValid = jwtUtil.verifyToken(invalidToken);

        // Assert
        Assertions.assertFalse(isValid);
    }
}
