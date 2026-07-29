package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "Category name is required")
        @Schema(description = "Nome da categoria", example = "Eletrônicos")
        String name
) {}
