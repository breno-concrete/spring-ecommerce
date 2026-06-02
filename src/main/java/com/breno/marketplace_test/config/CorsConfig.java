package com.breno.marketplace_test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // O Spring vai no .yaml que estiver ativo e injeta o valor nessa variável
    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a regra para TODOS os endpoints (ex: /produtos, /pedidos)
                        .allowedOrigins(allowedOrigins) // Usa a lista de convidados dinâmica
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
