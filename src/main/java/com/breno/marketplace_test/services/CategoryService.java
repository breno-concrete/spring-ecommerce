package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.CategoryRequestDTO;
import com.breno.marketplace_test.dtos.CategoryResponseDTO;
import com.breno.marketplace_test.mappers.CategoryMapper;
import com.breno.marketplace_test.models.Category;
import com.breno.marketplace_test.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponseDTO> findAll() {
        log.info("Buscando todas as categorias");
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    public CategoryResponseDTO findCategoryById(Long id) {
        return categoryMapper.toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!")));
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryDTO) {
        log.info("Salvando nova categoria: {}", categoryDTO.name());
        Category category = new Category();
        category.setName(categoryDTO.name());

        Category savedCategory = categoryRepository.save(category);
        log.info("Categoria salva com sucesso. ID: {}, Nome: {}", savedCategory.getId(), savedCategory.getName());
        return convertToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDTO) {
        log.info("Atualizando categoria com ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Categoria com ID {} não encontrada para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        category.setName(categoryDTO.name());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Categoria com ID {} atualizada com sucesso. Novo nome: {}", id, updatedCategory.getName());
        return convertToResponseDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        log.info("Deletando categoria com ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Categoria com ID {} não encontrada para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        categoryRepository.delete(category);
        log.info("Categoria com ID {} deletada com sucesso", id);
    }

    private CategoryResponseDTO convertToResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName()
        );
    }
}

