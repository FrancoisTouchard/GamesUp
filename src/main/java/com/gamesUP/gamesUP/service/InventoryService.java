package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.InventoryDTO;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    List<InventoryDTO> findAll();

    InventoryDTO findById(UUID id);

    InventoryDTO findByGameId(UUID gameId);

    InventoryDTO create(InventoryDTO inventory);

    InventoryDTO partialUpdate(UUID id, InventoryDTO inventory);

    void deleteById(UUID id);
}
