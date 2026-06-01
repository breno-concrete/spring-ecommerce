package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.models.Order;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequestDTO orderDTO);

    OrderResponseDTO toDTO(Order order);

    List<OrderResponseDTO> toDTOList(List<Order> orders);
}
