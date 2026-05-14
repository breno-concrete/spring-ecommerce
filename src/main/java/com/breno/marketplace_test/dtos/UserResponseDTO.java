package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(
        @NotNull(message = "ID is required")
        Long id,
        @NotBlank(message = "Name is required")
        String nome,
        @NotBlank(message = "Email is required")
        String email
) {
    // Um construtor que já converte a Entity para DTO automaticamente
    public UserResponseDTO(User usuario) {
        this(usuario.getId(), usuario.getFullName(), usuario.getEmail());
    }
}
