package com.breno.marketplace_test.mappers;


import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;

import com.breno.marketplace_test.models.Product;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO productDTO);

    ProductResponseDTO toDTO(Product product);

    List<ProductResponseDTO> toDTOList(List<Product> products);
}
