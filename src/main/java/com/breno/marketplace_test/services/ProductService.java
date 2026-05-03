package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setUnit(productDTO.unit());
        product.setCategory(productDTO.category());
        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }

    public ProductResponseDTO updateProduct(Integer id, ProductRequestDTO product) {
        Product newProduct = productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );
        newProduct.setName(product.name());
        newProduct.setPrice(product.price());
        newProduct.setUnit(product.unit());
        newProduct.setCategory(product.category());
        Product updatedProduct = productRepository.save(newProduct);

        return convertToResponseDTO(updatedProduct);

    }

    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!"));
        productRepository.delete(product);
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

