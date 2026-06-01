package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageResponseDTO(

        Long id,

        String url,

        Long productId
) {}
