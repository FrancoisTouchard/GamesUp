package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDTO> findAll();

    CategoryDTO findById(UUID id);

    CategoryDTO create(CategoryDTO game);

    CategoryDTO update(UUID id, CategoryDTO game);

    CategoryDTO partialUpdate(UUID id, CategoryDTO game);

    void deleteById(UUID id);
}
