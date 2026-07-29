package com.breno.marketplace_test.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long id,

        @NotNull(message = "Product ID is required")
        @Schema(description = "ID do produto associado ao item do pedido", example = "1")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        @Schema(description = "Quantidade do produto no item do pedido", example = "2")
        Integer quantity,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        @Schema(description = "Preço unitário do produto no item do pedido", example = "1999.90")
        BigDecimal pricePurchased
) {}
