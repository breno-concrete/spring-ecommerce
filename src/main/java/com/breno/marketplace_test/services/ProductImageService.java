package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductImageRequestDTO;
import com.breno.marketplace_test.dtos.ProductImageResponseDTO;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.models.ProductImage;
import com.breno.marketplace_test.repositories.ProductImageRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    public List<ProductImage> findAll() {
        return productImageRepository.findAll();
    }

    public ProductImage findProductImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    public ProductImageResponseDTO saveProductImage(ProductImageRequestDTO imageDTO) {
        log.info("Salvando nova imagem para o produto: {}", imageDTO.productId());
        Product product = productRepository.findById(Math.toIntExact(imageDTO.productId()))
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado ao salvar imagem", imageDTO.productId());
                    return new IllegalStateException("Product " + imageDTO.productId() + " not found!");
                });

        ProductImage image = new ProductImage();
        image.setUrl(imageDTO.url());
        image.setProduct(product);

        ProductImage savedImage = productImageRepository.save(image);
        log.info("Imagem de produto salva com sucesso. ID: {}, URL: {}, Produto: {}",
                savedImage.getId(), savedImage.getUrl(), product.getName());
        return convertToResponseDTO(savedImage);
    }

    public ProductImageResponseDTO updateProductImage(Long id, ProductImageRequestDTO imageDTO) {
        log.info("Atualizando imagem de produto com ID: {}", id);
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Imagem de produto com ID {} não encontrada para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        Product product = productRepository.findById(Math.toIntExact(imageDTO.productId()))
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado ao atualizar imagem", imageDTO.productId());
                    return new IllegalStateException("Product " + imageDTO.productId() + " not found!");
                });

        image.setUrl(imageDTO.url());
        image.setProduct(product);

        ProductImage updatedImage = productImageRepository.save(image);
        log.info("Imagem de produto com ID {} atualizada com sucesso. Nova URL: {}", id, updatedImage.getUrl());
        return convertToResponseDTO(updatedImage);
    }

    public void deleteProductImage(Long id) {
        log.info("Deletando imagem de produto com ID: {}", id);
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Imagem de produto com ID {} não encontrada para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        productImageRepository.delete(image);
        log.info("Imagem de produto com ID {} deletada com sucesso", id);
    }

    private ProductImageResponseDTO convertToResponseDTO(ProductImage image) {
        return new ProductImageResponseDTO(
                image.getId(),
                image.getUrl(),
                image.getProduct().getId()
        );
    }
}

