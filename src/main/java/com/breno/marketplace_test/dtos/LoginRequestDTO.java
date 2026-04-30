package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//data transfer object para transportas os dados de login do usuário, com validação para garantir que o email e a password sejam fornecidos e que o email seja válido
public record LoginRequestDTO(
        @NotBlank(message = "Email is required") //não vazio
        @Email(message = "Email must be valid") //formato correto e domínio correto
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password
) {}