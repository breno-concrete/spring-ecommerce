package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO (
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    String password,

    String phone
){}


