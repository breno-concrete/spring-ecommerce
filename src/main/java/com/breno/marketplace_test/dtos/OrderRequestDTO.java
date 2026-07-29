package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "Order status is required")
        OrderStatus orderStatus,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Delivery address ID is required")
        Long deliveryAddressId,

        @NotEmpty(message = "The order must contain at least one item")
        @Valid
        List<OrderItemDTO> items
) {}

