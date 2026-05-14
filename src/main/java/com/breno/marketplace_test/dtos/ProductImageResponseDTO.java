package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageResponseDTO(
        @NotNull(message = "ID is required")
        Long id,
        @NotBlank(message = "URL is required")
        String url,
        @NotNull(message = "Product ID is required")
        Long productId
) {}
