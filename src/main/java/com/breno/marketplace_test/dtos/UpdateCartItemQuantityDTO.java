package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemQuantityDTO(
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima deve ser 1")
        Integer quantity
) {}