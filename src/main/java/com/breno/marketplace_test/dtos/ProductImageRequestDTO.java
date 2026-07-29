package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageRequestDTO(
        @NotBlank(message = "Image URL is required")
        @Schema(description = "URL da imagem do produto", example = "https://example.com/images/product-image.jpg")
        String url,

        @NotNull(message = "Product ID is required")
        @Schema(description = "ID do produto ao qual a imagem pertence", example = "1")
        Long productId
) {}
