package com.breno.marketplace_test.dtos;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    // Construtor customizado para definir o tipo de token padrão como "Bearer"
    public RefreshTokenResponseDTO(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}

