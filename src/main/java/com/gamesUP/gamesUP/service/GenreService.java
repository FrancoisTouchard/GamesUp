package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GenreDTO;

import java.util.List;
import java.util.UUID;

public interface GenreService {
    List<GenreDTO> findAll();
    GenreDTO findById(UUID id);
    GenreDTO create(GenreDTO genre);
    GenreDTO update(UUID id, GenreDTO genre);
    GenreDTO partialUpdate(UUID id, GenreDTO genre);
    void deleteById(UUID id);
}
