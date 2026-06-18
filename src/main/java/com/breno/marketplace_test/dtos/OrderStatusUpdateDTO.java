package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateDTO(
        @NotNull(message = "O status não pode ser nulo")
        OrderStatus status
) {}
