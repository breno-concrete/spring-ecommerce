package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ShoppingCartRequestDTO(
        @NotNull(message = "User ID is required")
        Long userId,

        List<CartItemDTO> items
) {}

