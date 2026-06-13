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

import com.gamesUP.gamesUP.dto.GenreDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Genre;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.GenreRepository;
import com.gamesUP.gamesUP.service.mapper.GenreMapper;

class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;
    private GenreDTO genreDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        genre = new Genre();
        genre.setId(id);
        genre.setName("RPG");

        genreDTO = new GenreDTO();
        genreDTO.setId(id);
        genreDTO.setName("RPG");
    }

    @Test
    void testFindAll() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        List<GenreDTO> result = genreService.findAll();

        assertEquals(1, result.size());
        assertEquals(genreDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        GenreDTO result = genreService.findById(id);

        assertEquals(genreDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> genreService.findById(id));
    }

    @Test
    void testCreate_Success() {
        when(genreRepository.existsByName("RPG")).thenReturn(false);
        when(genreMapper.toEntity(genreDTO)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        GenreDTO result = genreService.create(genreDTO);

        assertEquals(genreDTO, result);
        verify(genreRepository).save(genre);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(genreRepository.existsByName("RPG")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> genreService.create(genreDTO));
        verify(genreRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        GenreDTO updateDTO = new GenreDTO();
        updateDTO.setName("Strategy");

        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        genreService.update(id, updateDTO);

        assertEquals("Strategy", genre.getName());
        verify(genreRepository).save(genre);
    }

    @Test
    void testUpdate_NotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> genreService.update(id, genreDTO));
    }

    @Test
    void testPartialUpdate() {
        GenreDTO updateDTO = new GenreDTO();
        updateDTO.setName("Strategy");

        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toDTO(genre)).thenReturn(genreDTO);

        genreService.partialUpdate(id, updateDTO);

        assertEquals("Strategy", genre.getName());
        verify(genreRepository).save(genre);
    }

    @Test
    void testDeleteById_Success() {
        Game game = new Game();
        game.setGenres(new java.util.ArrayList<>(List.of(genre)));

        when(genreRepository.existsById(id)).thenReturn(true);
        when(gameRepository.findByGenresId(id)).thenReturn(List.of(game));

        genreService.deleteById(id);

        assertTrue(game.getGenres().isEmpty());
        verify(gameRepository).save(game);
        verify(genreRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(genreRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> genreService.deleteById(id));
        verify(genreRepository, never()).deleteById(id);
    }
}
