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
}
