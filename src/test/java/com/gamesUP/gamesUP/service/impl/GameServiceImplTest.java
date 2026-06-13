package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceInUseException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Genre;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.GenreRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import com.gamesUP.gamesUP.service.mapper.GameMapper;

class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PurchaseLineRepository purchaseLineRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game game;
    private GameDTO gameDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        game = new Game();
        game.setId(id);
        game.setName("Test Game");
        game.setNumEdition(1);
        game.setPrice(BigDecimal.valueOf(59.99));

        gameDTO = new GameDTO();
        gameDTO.setId(id);
        gameDTO.setName("Test Game");
        gameDTO.setNumEdition(1);
        gameDTO.setPrice(BigDecimal.valueOf(59.99));
    }

    @Test
    void testFindAll() {
        when(gameRepository.findAll()).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = gameService.findAll();

        assertEquals(1, result.size());
        assertEquals(gameDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        GameDTO result = gameService.findById(id);

        assertEquals(gameDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findById(id));
    }

    @Test
    void testCreate_Success() {
        when(gameRepository.existsByName("Test Game")).thenReturn(false);
        when(gameMapper.toEntity(gameDTO)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        GameDTO result = gameService.create(gameDTO);

        assertEquals(gameDTO, result);
        verify(gameRepository).save(game);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(gameRepository.existsByName("Test Game")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> gameService.create(gameDTO));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void testUpdate_AllFields() {
        Author author = new Author();
        author.setName("Author A");
        Category category = new Category();
        category.setName("Category A");
        Genre genre = new Genre();
        genre.setName("Genre A");
        Publisher publisher = new Publisher();
        publisher.setName("Publisher A");

        GameDTO updateDTO = new GameDTO();
        updateDTO.setName("Updated Game");
        updateDTO.setNumEdition(2);
        updateDTO.setPrice(BigDecimal.valueOf(39.99));
        updateDTO.setAuthorNames(List.of("Author A"));
        updateDTO.setCategoryNames(List.of("Category A"));
        updateDTO.setGenreNames(List.of("Genre A"));
        updateDTO.setPublisherName("Publisher A");

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(authorRepository.findByName("Author A")).thenReturn(Optional.of(author));
        when(categoryRepository.findByName("Category A")).thenReturn(Optional.of(category));
        when(genreRepository.findByName("Genre A")).thenReturn(Optional.of(genre));
        when(publisherRepository.findByName("Publisher A")).thenReturn(Optional.of(publisher));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        gameService.update(id, updateDTO);

        assertEquals("Updated Game", game.getName());
        assertEquals(2, game.getNumEdition());
        assertEquals(BigDecimal.valueOf(39.99), game.getPrice());
        assertEquals(List.of(author), game.getAuthors());
        assertEquals(List.of(category), game.getCategories());
        assertEquals(List.of(genre), game.getGenres());
        assertEquals(publisher, game.getPublisher());
        verify(gameRepository).save(game);
    }

    @Test
    void testUpdate_NotFound() {
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.update(id, gameDTO));
    }

    @Test
    void testUpdate_AuthorNotFound() {
        GameDTO updateDTO = new GameDTO();
        updateDTO.setAuthorNames(List.of("Unknown Author"));

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(authorRepository.findByName("Unknown Author")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.update(id, updateDTO));
    }

    @Test
    void testPartialUpdate() {
        GameDTO updateDTO = new GameDTO();
        updateDTO.setName("Partially Updated Game");

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        gameService.partialUpdate(id, updateDTO);

        assertEquals("Partially Updated Game", game.getName());
        verify(gameRepository).save(game);
    }

    @Test
    void testPartialUpdate_NotFound() {
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.partialUpdate(id, gameDTO));
    }

    @Test
    void testDeleteById_Success() {
        when(gameRepository.existsById(id)).thenReturn(true);
        when(purchaseLineRepository.existsByGameId(id)).thenReturn(false);

        gameService.deleteById(id);

        verify(gameRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(gameRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteById(id));
        verify(gameRepository, never()).deleteById(id);
    }

    @Test
    void testDeleteById_InUse() {
        when(gameRepository.existsById(id)).thenReturn(true);
        when(purchaseLineRepository.existsByGameId(id)).thenReturn(true);

        assertThrows(ResourceInUseException.class, () -> gameService.deleteById(id));
        verify(gameRepository, never()).deleteById(id);
    }

    @Test
    void testFindByCategoryName() {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Action");

        when(categoryRepository.findByName("Action")).thenReturn(Optional.of(category));
        when(gameRepository.findByCategoriesId(category.getId())).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = gameService.findByCategoryName("Action");

        assertEquals(List.of(gameDTO), result);
    }

    @Test
    void testFindByCategoryName_NotFound() {
        when(categoryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findByCategoryName("Unknown"));
    }

    @Test
    void testFindByGenreName() {
        Genre genre = new Genre();
        genre.setId(UUID.randomUUID());
        genre.setName("RPG");

        when(genreRepository.findByName("RPG")).thenReturn(Optional.of(genre));
        when(gameRepository.findByGenresId(genre.getId())).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = gameService.findByGenreName("RPG");

        assertEquals(List.of(gameDTO), result);
    }

    @Test
    void testFindByGenreName_NotFound() {
        when(genreRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findByGenreName("Unknown"));
    }

    @Test
    void testFindByPublisherName() {
        Publisher publisher = new Publisher();
        publisher.setId(UUID.randomUUID());
        publisher.setName("Test Publisher");

        when(publisherRepository.findByName("Test Publisher")).thenReturn(Optional.of(publisher));
        when(gameRepository.findByPublisherId(publisher.getId())).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = gameService.findByPublisherName("Test Publisher");

        assertEquals(List.of(gameDTO), result);
    }

    @Test
    void testFindByPublisherName_NotFound() {
        when(publisherRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findByPublisherName("Unknown"));
    }

    @Test
    void testFindByAuthorName() {
        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setName("Test Author");

        when(authorRepository.findByName("Test Author")).thenReturn(Optional.of(author));
        when(gameRepository.findByAuthorsId(author.getId())).thenReturn(List.of(game));
        when(gameMapper.toDTO(game)).thenReturn(gameDTO);

        List<GameDTO> result = gameService.findByAuthorName("Test Author");

        assertEquals(List.of(gameDTO), result);
    }

    @Test
    void testFindByAuthorName_NotFound() {
        when(authorRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findByAuthorName("Unknown"));
    }
}
