package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message ="Name is required")
        @Size(max=255)
        @Schema(description = "Nome descritivo do produto", example = "Smartphone Samsung Galaxy")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        @DecimalMin("0.01")
        @Schema(description = "Preço unitário do produto", example = "1999.90")
        BigDecimal price,

        @NotNull(message = "Unit is required")
        @Positive(message = "Unit must be positive")
        @Min(0)
        @Schema(description = "Quantidade disponível em estoque", example = "50")
        Integer unit,

        @NotNull(message = "Category is required")
        @Schema(description = "ID da categoria à qual o produto pertence", example = "2")
        Long category_id

)
{}
