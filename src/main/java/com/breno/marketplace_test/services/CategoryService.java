package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.CategoryRequestDTO;
import com.breno.marketplace_test.dtos.CategoryResponseDTO;
import com.breno.marketplace_test.models.Category;
import com.breno.marketplace_test.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());

        Category savedCategory = categoryRepository.save(category);
        return convertToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));

        category.setName(categoryDTO.name());

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponseDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        categoryRepository.delete(category);
    }

    private CategoryResponseDTO convertToResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }
}

