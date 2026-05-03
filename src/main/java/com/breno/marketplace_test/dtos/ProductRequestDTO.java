package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message ="Name is required")
        String name,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Unit is required")
        @Positive(message = "Unit must be positive")
        Integer unit,

        @NotNull(message = "Category is required")
        Category category

)
{}
