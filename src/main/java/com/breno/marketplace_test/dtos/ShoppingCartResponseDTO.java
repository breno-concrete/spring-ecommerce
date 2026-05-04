package com.breno.marketplace_test.dtos;

import java.util.List;

public record ShoppingCartResponseDTO(
        Long id,
        Long userId,
        List<CartItemDTO> items
) {}

