package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.CategoryRequestDTO;
import com.breno.marketplace_test.dtos.CategoryResponseDTO;
import com.breno.marketplace_test.mappers.CategoryMapper;
import com.breno.marketplace_test.models.Category;
import com.breno.marketplace_test.repositories.CategoryRepository;
import com.breno.marketplace_test.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Gerenciamento e listagem de categorias de produtos")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;




    @GetMapping
    @Operation(summary = "List all categories", description = "Returns a list of all categories")
    public ResponseEntity<Page<CategoryResponseDTO>>getCategories(@ParameterObject Pageable pageable) {
        log.info("Requisição GET para listar todas as categorias");

        Page<CategoryResponseDTO> page = categoryService.findAll(pageable);

        return ResponseEntity.ok(page);
    }



    @PostMapping
    @Operation(summary = "Create Category", description = "Creates a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO category) {
        log.info("Requisição POST para criar nova categoria: {}", category.name());
        CategoryResponseDTO response = categoryService.saveCategory(category);
        log.info("Categoria criada com sucesso. ID: {}", response.id());
        return ResponseEntity.status(201).body(response);
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
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Requisição DELETE para deletar categoria com ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Categoria com ID {} deletada com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}

