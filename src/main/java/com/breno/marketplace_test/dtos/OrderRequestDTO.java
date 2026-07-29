package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "Order status is required")
        @Schema(description = "Status do pedido", example = "PENDING")
        OrderStatus orderStatus,

        @NotNull(message = "User ID is required")
        @Schema(description = "ID único do usuário que fez o pedido", example = "1")
        Long userId,

        @NotNull(message = "Delivery address ID is required")
        @Schema(description = "ID do endereço de entrega", example = "1")
        Long deliveryAddressId,

        @NotEmpty(message = "The order must contain at least one item")
        @Valid
        @Schema(description = "Lista de itens que compõem o pedido", example = "[{\"productId\": 1, \"quantity\": 2}, {\"productId\": 2, \"quantity\": 1}]")
        List<OrderItemDTO> items
) {}

