package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductImageRequestDTO;
import com.breno.marketplace_test.dtos.ProductImageResponseDTO;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.models.ProductImage;
import com.breno.marketplace_test.repositories.ProductImageRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Product product = productRepository.findById(Math.toIntExact(imageDTO.productId()))
                .orElseThrow(() -> new IllegalStateException("Product " + imageDTO.productId() + " not found!"));

        ProductImage image = new ProductImage();
        image.setUrl(imageDTO.url());
        image.setProduct(product);

        ProductImage savedImage = productImageRepository.save(image);
        return convertToResponseDTO(savedImage);
    }

    public ProductImageResponseDTO updateProductImage(Long id, ProductImageRequestDTO imageDTO) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));

        Product product = productRepository.findById(Math.toIntExact(imageDTO.productId()))
                .orElseThrow(() -> new IllegalStateException("Product " + imageDTO.productId() + " not found!"));

        image.setUrl(imageDTO.url());
        image.setProduct(product);

        ProductImage updatedImage = productImageRepository.save(image);
        return convertToResponseDTO(updatedImage);
    }

    public void deleteProductImage(Long id) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        productImageRepository.delete(image);
    }

    private ProductImageResponseDTO convertToResponseDTO(ProductImage image) {
        return new ProductImageResponseDTO(
                image.getId(),
                image.getUrl(),
                image.getProduct().getId()
        );
    }
}

