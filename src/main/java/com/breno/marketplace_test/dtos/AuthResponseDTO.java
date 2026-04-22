package com.breno.marketplace_test.dtos;

public record AuthResponseDTO(
        String token,
        String email,
        String message
) {
    public AuthResponseDTO(String token, String email) {
        this(token, email, "Authentication successful"); // resposta com os dados necessários
    }
}