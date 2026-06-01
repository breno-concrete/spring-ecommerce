package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.ProductFilterDTO;
import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> search(
            @ModelAttribute ProductFilterDTO filter,
            @ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<ProductResponseDTO> result = productService.searchProducts(filter, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create Product", description = "Creates a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO product){
        log.info("Requisição POST para criar novo produto: {}", product.name());
        ProductResponseDTO response = productService.saveProduct(product);
        log.info("Produto criado com sucesso. ID: {}", response.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Product", description = "Returns a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ProductResponseDTO getProductById(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Product", description = "Updates a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO product){
        log.info("Requisição PUT para atualizar produto com ID: {}", id);
        ProductResponseDTO response = productService.updateProduct(id, product);
        log.info("Produto com ID {} atualizado com sucesso", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Product", description = "Deletes a product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        log.info("Requisição DELETE para deletar produto com ID: {}", id);
        productService.deleteProduct(id);
        log.info("Produto com ID {} deletado com sucesso", id);
        return ResponseEntity.ok("Product deleted successfully!");
    }
}

