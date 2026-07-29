package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record ProductFilterDTO(
        String name,

        @DecimalMin("0")
        BigDecimal minPrice,

        @DecimalMin("0")
        BigDecimal maxPrice,
        Long categoryId
) {}
