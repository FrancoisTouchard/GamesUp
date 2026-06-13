package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gamesUP.gamesUP.dto.InventoryDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import com.gamesUP.gamesUP.service.mapper.InventoryMapper;

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private InventoryDTO inventoryDTO;
    private Game game;
    private UUID id;
    private UUID gameId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        gameId = UUID.randomUUID();

        game = new Game();
        game.setId(gameId);
        game.setName("Test Game");

        inventory = new Inventory();
        inventory.setId(id);
        inventory.setGame(game);
        inventory.setStock(10);

        inventoryDTO = new InventoryDTO();
        inventoryDTO.setId(id);
        inventoryDTO.setGameName("Test Game");
        inventoryDTO.setStock(10);
    }

    @Test
    void testFindAll() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));
        when(inventoryMapper.toDTO(inventory)).thenReturn(inventoryDTO);

        List<InventoryDTO> result = inventoryService.findAll();

        assertEquals(1, result.size());
        assertEquals(inventoryDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.toDTO(inventory)).thenReturn(inventoryDTO);

        InventoryDTO result = inventoryService.findById(id);

        assertEquals(inventoryDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.findById(id));
    }

    @Test
    void testFindByGameId_Found() {
        when(inventoryRepository.findByGameId(gameId)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.toDTO(inventory)).thenReturn(inventoryDTO);

        InventoryDTO result = inventoryService.findByGameId(gameId);

        assertEquals(inventoryDTO, result);
    }

    @Test
    void testFindByGameId_NotFound() {
        when(inventoryRepository.findByGameId(gameId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.findByGameId(gameId));
    }

    @Test
    void testCreate_Success() {
        when(inventoryMapper.toEntity(inventoryDTO)).thenReturn(inventory);
        when(inventoryRepository.existsByGameId(gameId)).thenReturn(false);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryMapper.toDTO(inventory)).thenReturn(inventoryDTO);

        InventoryDTO result = inventoryService.create(inventoryDTO);

        assertEquals(inventoryDTO, result);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testCreate_AlreadyExists() {
        when(inventoryMapper.toEntity(inventoryDTO)).thenReturn(inventory);
        when(inventoryRepository.existsByGameId(gameId)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> inventoryService.create(inventoryDTO));
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void testPartialUpdate() {
        InventoryDTO updateDTO = new InventoryDTO();
        updateDTO.setStock(25);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryMapper.toDTO(inventory)).thenReturn(inventoryDTO);

        inventoryService.partialUpdate(id, updateDTO);

        assertEquals(25, inventory.getStock());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testPartialUpdate_NotFound() {
        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.partialUpdate(id, inventoryDTO));
    }

    @Test
    void testDeleteById_Success() {
        when(inventoryRepository.existsById(id)).thenReturn(true);

        inventoryService.deleteById(id);

        verify(inventoryRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(inventoryRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.deleteById(id));
        verify(inventoryRepository, never()).deleteById(id);
    }
}
