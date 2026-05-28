package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductFilterDTO;
import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.models.Category;
import com.breno.marketplace_test.repositories.CategoryRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.repositories.spec.ProductSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductResponseDTO saveProduct(ProductRequestDTO productDTO) {
        log.info("Salvando novo produto: {}", productDTO.name());
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setUnit(productDTO.unit());


        product.setCategory(findCategoryOrThrow(productDTO.category_id()));


        Product savedProduct = productRepository.save(product);
        log.info("Produto salvo com sucesso. ID: {}, Nome: {}, Preço: {}",
                savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
        return convertToResponseDTO(savedProduct);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO product) {
        log.info("Atualizando produto com ID: {}", id);
        Product newProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });
        newProduct.setName(product.name());
        newProduct.setPrice(product.price());
        newProduct.setUnit(product.unit());

        newProduct.setCategory(findCategoryOrThrow(product.category_id()));

        Product updatedProduct = productRepository.save(newProduct);
        log.info("Produto com ID {} atualizado com sucesso. Novo nome: {}", id, updatedProduct.getName());
        return convertToResponseDTO(updatedProduct);

    }

    public void deleteProduct(Long id) {
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

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Categoria com ID {} não encontrada", categoryId);
                    return new IllegalStateException("Categoria " + categoryId + " não encontrada!");
                });
    }


    public Page<ProductResponseDTO> searchProducts(ProductFilterDTO filter, Pageable pageable){
        Specification<Product> spec = ProductSpec.withFilters(filter);

        Page<Product> productsPage = productRepository.findAll(spec, pageable);

        return productsPage.map(ProductResponseDTO::new);

    }
}

