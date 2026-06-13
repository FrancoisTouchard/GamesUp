package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.mapper.CategoryMapper;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        category = new Category();
        category.setId(id);
        category.setName("Action");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(id);
        categoryDTO.setName("Action");
    }

    @Test
    void testFindAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));
        verify(categoryRepository).findAll();
    }

    @Test
    void testFindById_Found() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.findById(id);

        assertEquals(categoryDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(id));
    }

    @Test
    void testCreate_Success() {
        when(categoryRepository.existsByName("Action")).thenReturn(false);
        when(categoryMapper.toEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.create(categoryDTO);

        assertEquals(categoryDTO, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(categoryRepository.existsByName("Action")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.create(categoryDTO));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Adventure");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        categoryService.update(id, updateDTO);

        assertEquals("Adventure", category.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testUpdate_NotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(id, categoryDTO));
    }

    @Test
    void testPartialUpdate() {
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Adventure");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(categoryDTO);

        categoryService.partialUpdate(id, updateDTO);

        assertEquals("Adventure", category.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testDeleteById_Success() {
        Game game = new Game();
        game.setCategories(new java.util.ArrayList<>(List.of(category)));

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(gameRepository.findByCategoriesId(id)).thenReturn(List.of(game));

        categoryService.deleteById(id);

        assertTrue(game.getCategories().isEmpty());
        verify(gameRepository).save(game);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteById(id));
        verify(categoryRepository, never()).deleteById(id);
    }
}
