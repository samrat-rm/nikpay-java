package com.example.nikPay.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    @Mock
    private SecretKey secretKey;

    @InjectMocks
    private JwtUtil jwtUtil;

    public JwtUtilTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken_ValidSubjectAndExpiration_ReturnsToken() {
        // Arrange
        String subject = "example@example.com";
        long expirationMillis = 3600000; // 1 hour
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        JwtUtil jwtUtil = new JwtUtil(secretKey, algorithm);
        // Act
        String token = jwtUtil.generateToken(subject, expirationMillis);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testParseToken_ValidToken_ReturnsClaims() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTM1NmUwOS0wNzUyLTRkMzgtOWQxZi1lYmVmYWEwMzUyODkiLCJpYXQiOjE2ODQyOTg2NjAsImV4cCI6MTY4NDMwMjI2MH0.zEqoWe6P6YDT1KpdLYLSudRMzJun4bqYFyXZoFzRnKA";

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        JwtUtil jwtUtil = new JwtUtil(secretKey, algorithm);

        // Mock the claims
        Claims mockClaims = mock(Claims.class);
        when(jwtUtil.parseToken(token)).thenReturn(mockClaims);

        // Act
        Claims claims = jwtUtil.parseToken(token);

        // Assert
        assertNotNull(claims);
        assertEquals(mockClaims, claims);
    }

    @Test
    void testVerifyToken_ValidToken_ReturnsTrue() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhNTM1NmUwOS0wNzUyLTRkMzgtOWQxZi1lYmVmYWEwMzUyODkiLCJpYXQiOjE2ODQyOTg2NjAsImV4cCI6MTY4NDMwMjI2MH0.zEqoWe6P6YDT1KpdLYLSudRMzJun4bqYFyXZoFzRnKA";

        // Mock the claims' expiration
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 1000)); // Expire after 1 second
        when(jwtUtil.parseToken(token)).thenReturn(mockClaims);

        // Act
        boolean isValid = jwtUtil.verifyToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testVerifyToken_ExpiredToken_ReturnsFalse() {
        // Arrange
        String token = "expired_token";

        // Mock the claims' expiration
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 1000)); // Expired 1 second ago
        when(jwtUtil.parseToken(token)).thenReturn(mockClaims);

        // Act
        boolean isValid = jwtUtil.verifyToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testVerifyToken_InvalidToken_ReturnsFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZjcwZDMxZi00MDMyLTRhNjgtYjc2YS01ODhlYjk1NWJiYjMiLCJpYXQiOjE2ODQzODQxNzcsImV4cCI6MTY4NDM4Nzc3N30.-aD809eY02rlcfQ9tiGI02xIj7KIkQzZ1NZsg5xBMRY";

        // Mock the parseToken method to throw an exception
        when(jwtUtil.parseToken(token)).thenThrow(new RuntimeException("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYjlkMGU4OS01YTc4LTRkOGQtYjY1MC01MmJiMTA5OTNjZTYiLCJpYXQiOjE2ODQzODM1OTcsImV4cCI6MTY4NDM4NzE5N30.PJ_k6ZveyaNDwWqUKNOfNAE5kdiWhPv0T8FQxIQXi94"));

        // Act
        boolean isValid = jwtUtil.verifyToken(token);

        // Assert
        assertFalse(isValid);
    }
}
