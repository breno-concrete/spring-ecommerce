package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.User;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email
) {
    // Um construtor que já converte a Entity para DTO automaticamente
    public UsuarioResponseDTO(User usuario) {
        this(usuario.getId(), usuario.getFullName(), usuario.getEmail());
    }
}
