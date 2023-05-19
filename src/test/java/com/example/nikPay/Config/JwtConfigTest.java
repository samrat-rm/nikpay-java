package com.example.nikPay.Config;

import com.example.nikPay.Config.JwtConfig;
import com.example.nikPay.Config.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtConfigTest {

    private JwtConfig jwtConfig;

    @BeforeEach
    public void setup() {
        jwtConfig = new JwtConfig();
    }

    @Test
    public void testSecretKey() {
        // Arrange
        String expectedSecretKey = "jave2425-secreai23143ain3413us1542512nuse-se352343crt-k5245eye-secreai";

        // Act
        SecretKey secretKey = jwtConfig.secretKey();

        // Assert
        byte[] expectedKeyBytes = expectedSecretKey.getBytes(StandardCharsets.UTF_8);
        Assertions.assertArrayEquals(expectedKeyBytes, secretKey.getEncoded());
    }

    @Test
    public void testJwtUtil() {
        // Arrange
        SecretKey secretKey = Keys.hmacShaKeyFor("test-1234!@#$%^&*()-secret-keytest-1234!@#$%^&*()-secret-key".getBytes());
        JwtUtil jwtUtil = jwtConfig.jwtUtil(secretKey);

        // Act
        String token = jwtUtil.generateToken("user123" , 360000);

        // Assert
        Assertions.assertNotNull(token);

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        String subject = claimsJws.getBody().getSubject();
        Assertions.assertEquals("user123", subject);

        Date expirationDate = claimsJws.getBody().getExpiration();
        Assertions.assertNotNull(expirationDate);
    }
}
