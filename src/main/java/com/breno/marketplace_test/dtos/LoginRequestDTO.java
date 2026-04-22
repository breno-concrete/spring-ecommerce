package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


//data transfer object para transportas os dados de login do usuário, com validação para garantir que o email e a senha sejam fornecidos e que o email seja válido

public record LoginRequestDTO(
        @NotBlank(message = "Email is required") //não vazio
        @Email(message = "Email must be valid") //formato correto e domínio correto
        String email,

        @NotBlank(message = "Password is required") //não vazio
        String password
) {}