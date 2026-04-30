package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts(){
        return productService.findAll();
    }

    @PostMapping
    public void createProduct(@RequestBody Product product){
        productService.saveProduct(product);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Product", description = "Returns a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Product getProductById(@PathVariable Integer id){
        return productService.findProductById(id);
    }
}

