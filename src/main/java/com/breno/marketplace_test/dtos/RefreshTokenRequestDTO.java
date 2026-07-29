package com.breno.marketplace_test.dtos;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "Refresh token is required")
        @Schema(description = "Token de atualização (refresh token) válido para gerar um novo token de acesso", example = "dGhpcy1pcy1hLXNhbXBsZS1yZWZyZXNoLXRva2VuLWV4YW1wbGU=")
        String refreshToken
) {}