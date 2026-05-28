package com.breno.marketplace_test.dtos;

import java.math.BigDecimal;

public record ProductFilterDTO(
        String name,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Long categoryId
) {}
