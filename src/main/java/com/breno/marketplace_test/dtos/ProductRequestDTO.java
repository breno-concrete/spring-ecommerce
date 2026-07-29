package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message ="Name is required")
        @Size(max=255)
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        @DecimalMin("0.01")
        BigDecimal price,

        @NotNull(message = "Unit is required")
        @Positive(message = "Unit must be positive")
        @Min(0)
        Integer unit,

        @NotNull(message = "Category is required")
        Long category_id

)
{}
