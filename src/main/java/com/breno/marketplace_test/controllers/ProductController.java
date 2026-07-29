package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.ProductFilterDTO;
import com.breno.marketplace_test.dtos.ProductRequestDTO;
import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Gerenciamento e catálogo de produtos do marketplace")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(
            summary = "Listar e filtrar produtos",
            description = "Retorna uma lista paginada de produtos. Permite a aplicação de filtros dinâmicos e ordenação pelos parâmetros da URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de produtos retornada com sucesso")
    })
    public ResponseEntity<Page<ProductResponseDTO>> search(
            @ModelAttribute ProductFilterDTO filter,
            @ParameterObject
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable){

        Page<ProductResponseDTO> result = productService.searchProducts(filter, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar novo produto",
            description = "Adiciona um novo produto ao catálogo do marketplace."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: preço negativo, nome em branco)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Requer privilégios específicos para cadastrar produtos)")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO product){
        log.info("Requisição POST para criar novo produto: {}", product.name());
        ProductResponseDTO response = productService.saveProduct(product);
        log.info("Produto criado com sucesso. ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes completos de um produto específico baseado no ID fornecido na URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado no banco de dados")
    })
    public ProductResponseDTO getProductById(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza as informações de um produto existente. Requer o ID do produto na URL e os novos dados no corpo da requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Sem permissão para alterar este produto)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO product){
        log.info("Requisição PUT para atualizar produto com ID: {}", id);
        ProductResponseDTO response = productService.updateProduct(id, product);
        log.info("Produto com ID {} atualizado com sucesso", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletar produto",
            description = "Remove permanentemente um produto do sistema com base no ID fornecido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso (No Content)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Sem permissão para deletar produtos)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        log.info("Requisição DELETE para deletar produto com ID: {}", id);
        productService.deleteProduct(id);
        log.info("Produto com ID {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}

