package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.ProductImageRequestDTO;
import com.breno.marketplace_test.dtos.ProductImageResponseDTO;
import com.breno.marketplace_test.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products/{productId}/images")
public class ProductImageController {

    private ProductImageService productImageService;

    @GetMapping
    public ResponseEntity<Page<ProductImageResponseDTO>> getImage(
            @ParameterObject
            Pageable pageable,
            Long productId){

        Page<ProductImageResponseDTO> images = productImageService.findImagesByProductId(pageable,productId);

        // Retorna 200 OK. Se a lista estiver vazia, retorna um array vazio [] no JSON.
        return ResponseEntity.ok(images);
    }

    @PostMapping
    public ResponseEntity<ProductImageResponseDTO> addImage(@RequestBody ProductImageRequestDTO imageDTO){

        ProductImageResponseDTO savedImage = productImageService.saveProductImage(imageDTO);
        return ResponseEntity.status(201).body(savedImage);
    }


    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long productId, @PathVariable Long imageId) {
        log.info("Requisição DELETE para remover imagem ID {} do produto ID {}", imageId, productId);

        productImageService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }
}
