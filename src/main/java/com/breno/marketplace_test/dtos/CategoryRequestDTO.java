package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "Category name is required")
        String name
) {}

