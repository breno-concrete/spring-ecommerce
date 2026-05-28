package com.breno.marketplace_test.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.breno.marketplace_test.config.JwtProperties;
import com.breno.marketplace_test.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    @Value("${JWT_SECRET}")
    private String secret;

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
        Date expiration = extractAllClaims(token).getExpiration(); //TEMPO DE EXPIRAÇÃO DO TOKEN
        long remaining = expiration.getTime() - System.currentTimeMillis(); //TEMPO RESTANTE PARA EXPIRAÇÃO
        return Math.max(remaining, 0); // RETORNA 0 (CASO NEGATIVO) OU O TEMPO QUE RESTA
    }

    private Claims extractAllClaims(String token) { // METDO AUXILIAR PARA EXTRAIR TODAS AS INFOS DENTRO DE UM TOKEN
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}