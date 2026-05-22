package com.breno.marketplace_test.dtos;

import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.models.ProductImage;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        Integer unit,
        Long categoryId,
        String categoryName,
        List<String> imageUrls
) {
    public ProductResponseDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getUnit(),
                // Evita NullPointerException caso a categoria seja nula
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,

                // Supondo que seu Produto tenha uma lista de entidades Imagem ou URLs
                // Se for uma lista de entidades, você extrai só a String da URL assim:
            /* product.getImages() != null ?
               product.getImages().stream().map(Image::getUrl).toList() :
               List.of() */

                // Se já for uma lista de Strings na entidade, basta passar direto:
                product.getImages() != null ?
                        product.getImages().stream()
                        .map(ProductImage::getUrl) // Supondo que o método na sua imagem seja getUrl()
                        .toList()
                        : List.of()
        );
    }
}