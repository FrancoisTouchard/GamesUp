package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.GenreDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Genre;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.GenreRepository;
import com.gamesUP.gamesUP.service.GenreService;
import com.gamesUP.gamesUP.service.mapper.GenreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreMapper genreMapper;

    @Override
    public List<GenreDTO> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO findById(UUID id) {
        return genreRepository.findById(id)
                .map(genreMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable"));
    }

    @Override
    @Transactional
    public GenreDTO create(GenreDTO genre) {
        if (genreRepository.existsByName(genre.getName())) {
            throw new ResourceAlreadyExistsException("Un genre avec ce nom existe déjà : " + genre.getName());
        }
        Genre genreToSave = genreMapper.toEntity(genre);
        return genreMapper.toDTO(genreRepository.save(genreToSave));
    }

    @Override
    @Transactional
    public GenreDTO update(UUID id, GenreDTO genre) {
        Genre existing = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable"));
        if (genre.getName() != null) existing.setName(genre.getName());
        return genreMapper.toDTO(genreRepository.save(existing));
    }

    @Override
    @Transactional
    public GenreDTO partialUpdate(UUID id, GenreDTO genre) {
        Genre existing = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre introuvable"));
        if (genre.getName() != null) existing.setName(genre.getName());
        return genreMapper.toDTO(genreRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre introuvable");
        }
        for (Game game : gameRepository.findByGenresId(id)) {
            game.getGenres().remove(game.getGenres().stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst().orElse(null));
            gameRepository.save(game);
        }
        genreRepository.deleteById(id);
    }
}
