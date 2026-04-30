package com.breno.marketplace_test.services;

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

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }
}

