package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GameDTO;

import java.util.List;
import java.util.UUID;

public interface GameService {
    List<GameDTO> findAll();

    GameDTO findById(UUID id);

    GameDTO create(GameDTO game);

    GameDTO update(UUID id, GameDTO game);

    GameDTO partialUpdate(UUID id, GameDTO game);

    void deleteById(UUID id);

    List<GameDTO> findByCategoryName(String categoryName);

    List<GameDTO> findByGenreName(String genreName);

    List<GameDTO> findByPublisherName(String publisherName);

    List<GameDTO> findByAuthorName(String authorName);

}
