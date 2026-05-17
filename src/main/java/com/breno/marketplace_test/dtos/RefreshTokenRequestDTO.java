package com.breno.marketplace_test.dtos;



import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {}