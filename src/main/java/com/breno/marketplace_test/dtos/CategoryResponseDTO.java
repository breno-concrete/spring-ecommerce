package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryResponseDTO(
        @NotNull(message = "ID is required")
        Long id,
        @NotBlank(message = "Name is required")
        String name
) {}
