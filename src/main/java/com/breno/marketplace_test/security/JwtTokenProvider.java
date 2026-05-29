package com.breno.marketplace_test.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.breno.marketplace_test.config.JwtProperties;
import com.breno.marketplace_test.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;


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
                .withClaim("type", "access")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusMillis(jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withIssuer("ecommerce")
                .withSubject(email)
                .withClaim("type", "refresh")
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now()
                        .plusMillis(jwtProperties.getRefreshExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String validateAndGetEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .withIssuer("ecommerce")
                    .withClaim("type", "access")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Token is invalid or expired");
        }
    }

    public String validateRefreshTokenAndGetEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .withIssuer("ecommerce")
                    .withClaim("type", "refresh") // EXIGE QUE SEJA UM REFRESH TOKEN
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("Refresh Token inválido ou expirado");
        }
    }
    public long getRemainingTtlMillis(String token) {
        try{
            DecodedJWT jwt = JWT.decode(token);
            Date expiration = jwt.getExpiresAt();

            if(expiration == null){
                return 0;
            }

            long remaining = expiration.getTime() - System.currentTimeMillis();
            return Math.max(remaining, 0);
        } catch(JWTDecodeException e){
            return 0;
        }
//
    }
}