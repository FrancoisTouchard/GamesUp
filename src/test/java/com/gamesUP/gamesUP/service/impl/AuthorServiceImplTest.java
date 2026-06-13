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

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.mapper.AuthorMapper;
import com.gamesUP.gamesUP.service.mapper.GameMapper;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        author = new Author();
        author.setId(id);
        author.setName("Test Author");

        authorDTO = new AuthorDTO();
        authorDTO.setId(id);
        authorDTO.setName("Test Author");
    }

    @Test
    void testFindAll() {
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        List<AuthorDTO> result = authorService.findAll();

        assertEquals(1, result.size());
        assertEquals(authorDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.findById(id);

        assertEquals(authorDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.findById(id));
    }

    @Test
    void testCreate_Success_WithoutGames() {
        when(authorRepository.existsByName("Test Author")).thenReturn(false);
        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.create(authorDTO);

        assertEquals(authorDTO, result);
        assertTrue(author.getGames().isEmpty());
        verify(authorRepository).save(author);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(authorRepository.existsByName("Test Author")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authorService.create(authorDTO));
        verify(authorRepository, never()).save(any());
    }

    @Test
    void testCreate_WithExistingGame() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setName("Existing Game");
        authorDTO.setGames(List.of(gameDTO));

        Game existingGame = new Game();
        existingGame.setName("Existing Game");

        when(authorRepository.existsByName("Test Author")).thenReturn(false);
        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(gameRepository.findByName("Existing Game")).thenReturn(Optional.of(existingGame));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        authorService.create(authorDTO);

        assertEquals(List.of(existingGame), author.getGames());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void testCreate_WithNewGame() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setName("New Game");
        authorDTO.setGames(List.of(gameDTO));

        Game newGame = new Game();
        newGame.setName("New Game");

        when(authorRepository.existsByName("Test Author")).thenReturn(false);
        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(gameRepository.findByName("New Game")).thenReturn(Optional.empty());
        when(gameMapper.toEntity(gameDTO)).thenReturn(newGame);
        when(gameRepository.save(newGame)).thenReturn(newGame);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        authorService.create(authorDTO);

        assertEquals(List.of(newGame), author.getGames());
        verify(gameRepository).save(newGame);
    }

    @Test
    void testUpdate() {
        AuthorDTO updateDTO = new AuthorDTO();
        updateDTO.setName("New Author Name");

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        authorService.update(id, updateDTO);

        assertEquals("New Author Name", author.getName());
        verify(authorRepository).save(author);
    }

    @Test
    void testUpdate_NotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.update(id, authorDTO));
    }

    @Test
    void testPartialUpdate() {
        AuthorDTO updateDTO = new AuthorDTO();
        updateDTO.setName("New Author Name");

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        authorService.partialUpdate(id, updateDTO);

        assertEquals("New Author Name", author.getName());
        verify(authorRepository).save(author);
    }

    @Test
    void testDeleteById_Success() {
        Game game = new Game();
        game.setAuthors(new java.util.ArrayList<>(List.of(author)));

        when(authorRepository.existsById(id)).thenReturn(true);
        when(gameRepository.findByAuthorsId(id)).thenReturn(List.of(game));

        authorService.deleteById(id);

        assertTrue(game.getAuthors().isEmpty());
        verify(gameRepository).save(game);
        verify(authorRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(authorRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteById(id));
        verify(authorRepository, never()).deleteById(id);
    }
}
