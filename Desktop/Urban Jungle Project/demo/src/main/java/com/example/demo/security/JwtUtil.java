package com.example.demo.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


@Component
public class JwtUtil {
     private static final String SECRET = "urbanjungle_super_secret_key_123";

    private final Algorithm algorithm =
            Algorithm.HMAC256(SECRET);

    // 1 hour
    private final long EXPIRATION_TIME = 60 * 60 * 1000;

    public String generateToken(String email) {

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .sign(algorithm);
    }

    public String extractEmail(String token) {

        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String token) {

        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
