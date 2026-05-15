package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        @NotBlank(message = "Access token is required")
        String accessToken,
        @NotBlank(message = "Refresh token is required")
        String refreshToken,
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Message is required")
        String message
) {
    public LoginResponseDTO(String token, String refreshToken, String email) {
        this(token, refreshToken, email, "Authentication successful"); // resposta com os dados necessários
    }
}