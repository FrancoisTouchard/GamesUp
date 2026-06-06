package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.*;
import com.gamesUP.gamesUP.service.GameService;
import com.gamesUP.gamesUP.service.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GameMapper gameMapper;

    @Override
    public List<GameDTO> findAll() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GameDTO findById(UUID id) {
        return gameRepository.findById(id)
                .map(gameMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
    }

    @Override
    @Transactional
    public GameDTO create(GameDTO game) {
        if (gameRepository.existsByName(game.getName())) {
            throw new ResourceAlreadyExistsException("Un jeu avec ce nom existe déjà : " + game.getName());
        }
        Game gameToSave = gameMapper.toEntity(game);
        return gameMapper.toDTO(gameRepository.save(gameToSave));
    }

    @Override
    @Transactional
    public GameDTO update(UUID id, GameDTO game) {
        Game existing = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
        if (game.getName() != null) existing.setName(game.getName());
        if (game.getNumEdition() != 0) existing.setNumEdition(game.getNumEdition());
        if (game.getAuthorNames() != null) {
            List<Author> authors = game.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setAuthors(authors);
        }
        if (game.getCategoryNames() != null) {
            List<Category> categories = game.getCategoryNames().stream()
                    .map(name -> categoryRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setCategories(categories);
        }
        if (game.getGenreNames() != null) {
            List<Genre> genres = game.getGenreNames().stream()
                    .map(name -> genreRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setGenres(genres);
        }
        if (game.getPublisherName() != null) {
            Publisher publisher = publisherRepository.findByName(game.getPublisherName())
                    .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable : " + game.getPublisherName()));
            existing.setPublisher(publisher);
        }
        return gameMapper.toDTO(gameRepository.save(existing));
    }

    @Override
    @Transactional
    public GameDTO partialUpdate(UUID id, GameDTO game) {
        Game existing = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
        if (game.getName() != null) existing.setName(game.getName());
        if (game.getNumEdition() != 0) existing.setNumEdition(game.getNumEdition());
        if (game.getAuthorNames() != null) {
            List<Author> authors = game.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setAuthors(authors);
        }
        if (game.getCategoryNames() != null) {
            List<Category> categories = game.getCategoryNames().stream()
                    .map(name -> categoryRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setCategories(categories);
        }
        if (game.getGenreNames() != null) {
            List<Genre> genres = game.getGenreNames().stream()
                    .map(name -> genreRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setGenres(genres);
        }
        if (game.getPublisherName() != null) {
            Publisher publisher = publisherRepository.findByName(game.getPublisherName())
                    .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable : " + game.getPublisherName()));
            existing.setPublisher(publisher);
        }
        return gameMapper.toDTO(gameRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Jeu introuvable");
        }
        gameRepository.deleteById(id);
    }

    @Override
    public List<GameDTO> findByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + categoryName));
        return gameRepository.findByCategoriesId(category.getId()).stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDTO> findByGenreName(String genreName) {
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable : " + genreName));
        return gameRepository.findByGenresId(genre.getId()).stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDTO> findByPublisherName(String publisherName) {
        Publisher publisher = publisherRepository.findByName(publisherName)
                .orElseThrow(() -> new ResourceNotFoundException("éditeur introuvable : " + publisherName));
        return gameRepository.findByPublisherId(publisher.getId()).stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDTO> findByAuthorName(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + authorName));
        return gameRepository.findByAuthorsId(author.getId()).stream()
                .map(gameMapper::toDTO)
                .collect(Collectors.toList());
    }


}
