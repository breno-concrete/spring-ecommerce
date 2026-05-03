package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Integer id,
        LocalDateTime creationTime,
        OrderStatus orderStatus,
        Long userId,
        Long deliveryAddressId,
        List<OrderItemDTO> items
) {}

