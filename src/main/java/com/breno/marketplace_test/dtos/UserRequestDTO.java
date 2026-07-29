package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO (
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Password is required")
    @Size(min=8, max=100)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#]).*$",
            message = "A senha deve conter pelo menos uma letra maiúscula, um número e um caractere especial"
    )
    String password,

    String phone
){}


