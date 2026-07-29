package com.breno.marketplace_test.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce API")
                        .version("1.0.0")
                        .description("API RESTful desenvolvida em Spring Boot para gerenciamento completo de um ecossistema de e-commerce, incluindo controle de usuários, produtos, carrinhos de compras e pedidos.")
                        .contact(new Contact()
                                .name("Breno Gomes Cardoso")
                                .url("https://github.com/breno-concrete")
                                .email("brenocount@gmail.com"))
                        .license(new License()
                                .url("https://opensource.org/licenses/MIT")
                                .name("MIT License")))

                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );


    }
}