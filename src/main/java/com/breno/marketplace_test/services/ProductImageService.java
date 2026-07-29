package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductImageRequestDTO;
import com.breno.marketplace_test.dtos.ProductImageResponseDTO;
import com.breno.marketplace_test.mappers.ProductImageMapper;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.models.ProductImage;
import com.breno.marketplace_test.repositories.ProductImageRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional; // USE ESTA
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;


    @Transactional(readOnly = true)
    public Page<ProductImageResponseDTO> findImagesByProductId(Pageable pageable,Long productId){
        log.info("Buscando imagens para o produto ID: {}", productId);

        if (!productRepository.existsById(productId)) {
            log.warn("Tentativa de buscar imagens para um produto inexistente. ID: {}", productId);
            throw new IllegalStateException("Product " + productId + " not found!");
        }


        Page<ProductImage> productImagePage = productImageRepository.findByProductId(pageable, productId);

        return productImagePage.map(productImageMapper::toDTO);

    }

    @Transactional
    public ProductImageResponseDTO saveProductImage(ProductImageRequestDTO imageDTO) {
        log.info("Salvando nova imagem para o produto: {}", imageDTO.productId());

        Product product = productRepository.findById(imageDTO.productId())
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

        return productImageMapper.toDTO(savedImage);
    }

    @Transactional
    public ProductImageResponseDTO updateProductImage(Long id, ProductImageRequestDTO imageDTO) {
        log.info("Atualizando imagem de produto com ID: {}", id);
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Imagem de produto com ID {} não encontrada para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        Product product = productRepository.findById(imageDTO.productId())
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado ao atualizar imagem", imageDTO.productId());
                    return new IllegalStateException("Product " + imageDTO.productId() + " not found!");
                });

        image.setUrl(imageDTO.url());
        image.setProduct(product);

        ProductImage updatedImage = productImageRepository.save(image);
        log.info("Imagem de produto com ID {} atualizada com sucesso. Nova URL: {}", id, updatedImage.getUrl());

        return productImageMapper.toDTO(updatedImage);
    }

    @Transactional
    public void deleteProductImage(Long productId, Long imageId) {
        log.info("Deletando imagem ID: {} do produto ID: {}", imageId, productId);

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.warn("Imagem de produto com ID {} não encontrada para deleção", imageId);
                    return new IllegalStateException(imageId + " not found!");
                });

        if (!image.getProduct().getId().equals(productId)) {
            log.warn("A imagem ID {} não pertence ao produto ID {}", imageId, productId);
            throw new IllegalStateException("A imagem não pertence ao produto especificado.");
        }

        productImageRepository.delete(image);
        log.info("Imagem de produto com ID {} deletada com sucesso", imageId);
    }
}

