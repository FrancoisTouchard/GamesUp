package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.InventoryDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    @Autowired
    private GameRepository gameRepository;

    public InventoryDTO toDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        if (inventory.getGame() != null) {
            dto.setGameName(inventory.getGame().getName());
        }
        dto.setStock(inventory.getStock());
        return dto;
    }

    public Inventory toEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        Game game = gameRepository.findByName(dto.getGameName())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + dto.getGameName()));
        inventory.setGame(game);
        inventory.setStock(dto.getStock());
        return inventory;
    }
}
