package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ShoppingCartRequestDTO(
        @NotNull(message = "User ID is required")
        @Schema(description = "ID único do usuário dono do carrinho", example = "1")
        Long userId,

        @Valid
        @Schema(description = "Lista de itens que compõem o carrinho de compras")
        List<CartItemDTO> items
) {}

