package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        return category;
    }
}
