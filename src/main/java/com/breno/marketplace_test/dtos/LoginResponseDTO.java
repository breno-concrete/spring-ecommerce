package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(

        String accessToken,

        String refreshToken,

        String email,

        String message
) {
    public LoginResponseDTO(String token, String refreshToken, String email) {
        this(token, refreshToken, email, "Authentication successful"); // resposta com os dados necessários
    }
}