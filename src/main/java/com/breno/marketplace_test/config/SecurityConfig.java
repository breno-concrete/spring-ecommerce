package com.breno.marketplace_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder é um bean que fornece a criptografia de senhas.
     *
     * Como funciona:
     * 1. encode("minhaSenha") → "$2a$10$..." (hash único)
     * 2. matches("minhaSenha", "$2a$10$...") → true/false
     *
     * O número 10 é o "strength" (custo computacional).
     * Quanto maior, mais seguro mas mais lento.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);  // Strength = 10 é o padrão
    }
}

