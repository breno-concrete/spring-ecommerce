package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Create Product", description = "Creates a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO product){
        return ResponseEntity.ok(productService.saveProduct(product));
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

    @PutMapping("{id}")
    @Operation(summary = "Update Product", description = "Updates a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Product", description = "Deletes a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }
}

