package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageRequestDTO(
        @NotBlank(message = "Image URL is required")
        String url,

        @NotNull(message = "Product ID is required")
        Long productId
) {}

