package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public ProductResponseDTO saveProduct(ProductRequestDTO productDTO) {
        log.info("Salvando novo produto: {}", productDTO.name());
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setUnit(productDTO.unit());
        product.setCategory(productDTO.category());
        Product savedProduct = productRepository.save(product);
        log.info("Produto salvo com sucesso. ID: {}, Nome: {}, Preço: {}",
                savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
        return convertToResponseDTO(savedProduct);
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }

    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product) {
        log.info("Atualizando produto com ID: {}", id);
        Product newProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });
        newProduct.setName(product.name());
        newProduct.setPrice(product.price());
        newProduct.setUnit(product.unit());
        newProduct.setCategory(product.category());
        Product updatedProduct = productRepository.save(newProduct);
        log.info("Produto com ID {} atualizado com sucesso. Novo nome: {}", id, updatedProduct.getName());
        return convertToResponseDTO(updatedProduct);

    }

    public void deleteProduct(Integer id) {
        log.info("Deletando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        productRepository.delete(product);
        log.info("Produto com ID {} deletado com sucesso", id);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        // Vamos extrair apenas as URLs das imagens (assumindo que ProductImage tem um campo url/path)
        List<String> imagesUrls = product.getImages().stream()
                .map(image -> image.getUrl()) // Mude .getUrl() para o nome real do campo na sua classe ProductImage
                .toList();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getUnit(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                imagesUrls
        );
    }
}

