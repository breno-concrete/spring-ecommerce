package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.CategoryRequestDTO;
import com.breno.marketplace_test.dtos.CategoryResponseDTO;
import com.breno.marketplace_test.models.Category;
import com.breno.marketplace_test.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    @GetMapping
    @Operation(summary = "List all categories", description = "Returns a list of all categories")
    public List<CategoryResponseDTO> getCategories() {
        log.info("Requisição GET para listar todas as categorias");
        return categoryService.findAll();
    }



    @PostMapping
    @Operation(summary = "Create Category", description = "Creates a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO category) {
        log.info("Requisição POST para criar nova categoria: {}", category.name());
        CategoryResponseDTO response = categoryService.saveCategory(category);
        log.info("Categoria criada com sucesso. ID: {}", response.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Category", description = "Returns a category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Category", description = "Updates a category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO category) {
        log.info("Requisição PUT para atualizar categoria com ID: {}", id);
        CategoryResponseDTO response = categoryService.updateCategory(id, category);
        log.info("Categoria com ID {} atualizada com sucesso", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Category", description = "Deletes a category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        log.info("Requisição DELETE para deletar categoria com ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Categoria com ID {} deletada com sucesso", id);
        return ResponseEntity.ok("Category deleted successfully!");
    }
}

