package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.ShoppingCartRequestDTO;
import com.breno.marketplace_test.dtos.ShoppingCartResponseDTO;
import com.breno.marketplace_test.models.ShoppingCart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ShoppingCart toEntity(ShoppingCartRequestDTO shoppingCartRequestDTO);

    ShoppingCartResponseDTO toDTO(ShoppingCart shoppingCart);

    List<ShoppingCartResponseDTO> toDTOList(List<ShoppingCart> shoppingCarts);


}
