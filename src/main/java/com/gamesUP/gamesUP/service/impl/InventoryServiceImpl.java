package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.InventoryDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import com.gamesUP.gamesUP.service.InventoryService;
import com.gamesUP.gamesUP.service.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public List<InventoryDTO> findAll() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDTO findById(UUID id) {
        return inventoryRepository.findById(id)
                .map(inventoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Stock introuvable : " + id));
    }

    @Override
    public InventoryDTO findByGameId(UUID gameId) {
        return inventoryRepository.findByGameId(gameId)
                .map(inventoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Stock introuvable pour le jeu : " + gameId));
    }

    @Override
    @Transactional
    public InventoryDTO create(InventoryDTO inventory) {
        Inventory toSave = inventoryMapper.toEntity(inventory);
        if (inventoryRepository.existsByGameId(toSave.getGame().getId())) {
            throw new ResourceAlreadyExistsException("Une entrée d'inventaire existe déjà pour ce jeu : " + inventory.getGameName());
        }
        return inventoryMapper.toDTO(inventoryRepository.save(toSave));
    }

    @Override
    @Transactional
    public InventoryDTO partialUpdate(UUID id, InventoryDTO inventory) {
        Inventory existing = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock introuvable : " + id));
        if (inventory.getStock() != 0) existing.setStock(inventory.getStock());
        return inventoryMapper.toDTO(inventoryRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stock introuvable : " + id);
        }
        inventoryRepository.deleteById(id);
    }
}
