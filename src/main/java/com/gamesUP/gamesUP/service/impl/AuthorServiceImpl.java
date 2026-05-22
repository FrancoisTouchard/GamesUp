package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Game;

import com.gamesUP.gamesUP.repository.AuthorRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.AuthorService;
import com.gamesUP.gamesUP.service.mapper.AuthorMapper;
import com.gamesUP.gamesUP.service.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Override
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO findById(UUID id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable"));
    }

    @Override
    @Transactional
    public AuthorDTO create(AuthorDTO author) {
        if (authorRepository.existsByName(author.getName())) {
            throw new ResourceAlreadyExistsException("Un auteur avec ce nom existe déjà : " + author.getName());
        }
        Author authorToSave = authorMapper.toEntity(author);
        authorToSave.setGames(resolveGames(author.getGames()));
        return authorMapper.toDTO(authorRepository.save(authorToSave));
    }

    @Override
    @Transactional
    public AuthorDTO update(UUID id, AuthorDTO author) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable"));
        if (author.getName() != null) existing.setName(author.getName());
        return authorMapper.toDTO(authorRepository.save(existing));
    }

    @Override
    @Transactional
    public AuthorDTO partialUpdate(UUID id, AuthorDTO author) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable"));
        if (author.getName() != null) existing.setName(author.getName());
        return authorMapper.toDTO(authorRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Auteur introuvable");
        }
        for (Game game : gameRepository.findByAuthorsId(id)) {
            game.getAuthors().remove(game.getAuthors().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst().orElse(null));
            gameRepository.save(game);
        }
        authorRepository.deleteById(id);
    }

    // If a game with the same name already exists, link it. If not, create it.
    private List<Game> resolveGames(List<GameDTO> gameDTOs) {
        if (gameDTOs == null || gameDTOs.isEmpty()) return Collections.emptyList();
        return gameDTOs.stream()
                .map(dto -> gameRepository.findByName(dto.getName())
                        .orElseGet(() -> gameRepository.save(gameMapper.toEntity(dto))))
                .collect(Collectors.toList());
    }
}
