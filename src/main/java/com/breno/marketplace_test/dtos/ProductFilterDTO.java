package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record ProductFilterDTO(
        String name,

        @DecimalMin("0")
        @Schema(description = "Preço mínimo do produto para filtragem", example = "100.00")
        BigDecimal minPrice,

        @DecimalMin("0")
        @Schema(description = "Preço máximo do produto para filtragem", example = "1000.00")
        BigDecimal maxPrice,
        Long categoryId
) {}
