package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GenreRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public GameDTO toDTO(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setNumEdition(game.getNumEdition());
        if (game.getCategories() != null) {
            dto.setCategoryNames(game.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toList()));
        }
        if (game.getGenres() != null) {
            dto.setGenreNames(game.getGenres().stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList()));
        }
        if (game.getAuthors() != null) {
            dto.setAuthorNames(game.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.toList()));
        }
        if (game.getPublisher() != null) {
            dto.setPublisherName(game.getPublisher().getName());
        }
        return dto;
    }

    public Game toEntity(GameDTO dto) {
        Game game = new Game();
        game.setName(dto.getName());
        game.setNumEdition(dto.getNumEdition());
        if (dto.getAuthorNames() != null) {
            List<Author> authors = dto.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + name)))
                    .collect(Collectors.toList());
            game.setAuthors(authors);
        } else {
            game.setAuthors(Collections.emptyList());
        }
        if (dto.getCategoryNames() != null) {
            List<Category> categories = dto.getCategoryNames().stream()
                    .map(name -> categoryRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + name)))
                    .collect(Collectors.toList());
            game.setCategories(categories);
        } else {
            game.setCategories(Collections.emptyList());
        }
        if (dto.getGenreNames() != null) {
            List<Genre> genres = dto.getGenreNames().stream()
                    .map(name -> genreRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable : " + name)))
                    .collect(Collectors.toList());
            game.setGenres(genres);
        } else {
            game.setGenres(Collections.emptyList());
        }
        if (dto.getPublisherName() != null) {
            Publisher publisher = publisherRepository.findByName(dto.getPublisherName())
                    .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable : " + dto.getPublisherName()));
            game.setPublisher(publisher);
        }
        return game;
    }
}
