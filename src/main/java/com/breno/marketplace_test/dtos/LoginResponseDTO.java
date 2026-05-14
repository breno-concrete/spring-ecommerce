package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        @NotBlank(message = "Token is required")
        String token,
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Message is required")
        String message
) {
    public LoginResponseDTO(String token, String email) {
        this(token, email, "Authentication successful"); // resposta com os dados necessários
    }
}