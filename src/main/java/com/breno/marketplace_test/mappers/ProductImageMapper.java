package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.ProductImageResponseDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.models.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductImageResponseDTO toDTO(ProductImage productImage);

    @Mapping(source = "productId", target = "product.id")
    ProductImage toEntity(ProductImageResponseDTO productImageDTO);

    List<ProductImageResponseDTO> toDTO(List<ProductImage> productImages);


}
