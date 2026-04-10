package com.breno.marketplace_test.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.breno.marketplace_test.config.JwtProperties;
import com.breno.marketplace_test.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String email) {
        return JWT.create()
                .withIssuer("ecommerce")
                .withSubject(email)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusMillis(jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withIssuer("ecommerce")
                .withSubject(email)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusMillis(jwtProperties.getRefreshExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String validateAndGetEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .withIssuer("ecommerce")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Token is invalid or expired");
        }
    }
}