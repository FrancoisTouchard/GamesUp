package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.CategoryService;
import com.gamesUP.gamesUP.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(UUID id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ResourceAlreadyExistsException("Une catégorie avec ce nom existe déjà : " + category.getName());
        }
        Category categoryToSave = categoryMapper.toEntity(category);
        return categoryMapper.toDTO(categoryRepository.save(categoryToSave));
    }

    @Override
    @Transactional
    public CategoryDTO update(UUID id, CategoryDTO category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
        if (category.getName() != null) existing.setName(category.getName());

        return categoryMapper.toDTO(categoryRepository.save(existing));
    }

    @Override
    @Transactional
    public CategoryDTO partialUpdate(UUID id, CategoryDTO category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
        if (category.getName() != null) existing.setName(category.getName());

        return categoryMapper.toDTO(categoryRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }
        for (Game game : gameRepository.findByCategoriesId(id)) {
            game.getCategories().remove(game.getCategories().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst().orElse(null));
            gameRepository.save(game);
        }
        categoryRepository.deleteById(id);
    }
}
