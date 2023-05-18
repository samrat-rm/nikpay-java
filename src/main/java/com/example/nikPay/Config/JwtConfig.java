package com.example.nikPay.Config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    // Replace this with your own secret key
    private static final String SECRET_KEY = "jave2425-secreai23143ain3413us1542512nuse-se352343crt-k5245eye-secreai";

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Bean
    public JwtUtil jwtUtil(SecretKey secretKey) {
        return new JwtUtil(secretKey, SignatureAlgorithm.HS256);
    }
}
