package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
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
        if (game.getAuthorNames() != null) {
            List<Author> authors = game.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setAuthors(authors);
        }
        if (game.getGenre() != null) existing.setGenre(game.getGenre());
        if (game.getNumEdition() != 0) existing.setNumEdition(game.getNumEdition());
        if (game.getCategoryName() != null) {
            Category category = categoryRepository.findByName(game.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + game.getCategoryName()));
            existing.setCategory(category);
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
        if (game.getAuthorNames() != null) {
            List<Author> authors = game.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setAuthors(authors);
        }
        if (game.getGenre() != null) existing.setGenre(game.getGenre());
        if (game.getNumEdition() != 0) existing.setNumEdition(game.getNumEdition());
        if (game.getCategoryName() != null) {
            Category category = categoryRepository.findByName(game.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + game.getCategoryName()));
            existing.setCategory(category);
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
}
