package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(

        Long id,

        String nome,

        String email
) {
    // Um construtor que já converte a Entity para DTO automaticamente
    public UserResponseDTO(User usuario) {
        this(usuario.getId(), usuario.getFullName(), usuario.getEmail());
    }
}
