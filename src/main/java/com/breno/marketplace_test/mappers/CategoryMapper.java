package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.CategoryRequestDTO;
import com.breno.marketplace_test.dtos.CategoryResponseDTO;

import com.breno.marketplace_test.models.Category;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDTO categoryDTO);

    CategoryResponseDTO toDTO(Category category);

    List<CategoryResponseDTO> toDTOList(List<Category> categories);
}
