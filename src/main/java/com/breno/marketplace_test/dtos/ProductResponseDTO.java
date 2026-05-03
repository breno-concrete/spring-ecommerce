package com.breno.marketplace_test.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer unit,
        Long categoryId,
        String categoryName,
        List<String> imageUrls
) {}