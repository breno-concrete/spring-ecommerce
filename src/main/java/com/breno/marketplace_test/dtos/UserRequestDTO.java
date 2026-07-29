package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO (
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Schema(description = "Endereço de email válido (usado para login)", example = "breno@exemplo.com")
    String email,

    @NotBlank(message = "Full name is required")
    @Schema(description = "Nome completo do usuário", example = "Breno Gomes")
    String fullName,

    @NotBlank(message = "Password is required")
    @Size(min=8, max=100)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#]).*$",
            message = "A senha deve conter pelo menos uma letra maiúscula, um número e um caractere especial"
    )
    @Schema(description = "Senha com no mínimo 8 caracteres, contendo maiúscula, número e caractere especial", example = "SenhaForte123!")
    String password,

    @Schema(description = "Número de telefone do usuário, no formato internacional", example = "+55 11 91234-5678")
    String phone
){}


