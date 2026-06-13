package com.gamesUP.gamesUP.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.exception.OutOfStockException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.model.PurchaseStatus;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.mapper.PurchaseMapper;

class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private Purchase purchase;
    private PurchaseDTO purchaseDTO;
    private AppUser user;
    private UUID id;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new AppUser();
        user.setId(userId);

        purchase = new Purchase();
        purchase.setId(id);
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.PENDING);
        purchase.setPurchaseLines(new ArrayList<>());

        purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(id);
        purchaseDTO.setUserId(userId);
        purchaseDTO.setStatus(PurchaseStatus.PENDING);
    }

    @Test
    void testFindAll() {
        when(purchaseRepository.findAll()).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        List<PurchaseDTO> result = purchaseService.findAll();

        assertEquals(1, result.size());
        assertEquals(purchaseDTO, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.findById(id);

        assertEquals(purchaseDTO, result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindById_NotFound() {
        when(purchaseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.findById(id));
    }

    @Test
    void testFindByUserId() {
        when(purchaseRepository.findByUserId(userId)).thenReturn(List.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        List<PurchaseDTO> result = purchaseService.findByUserId(userId);

        assertEquals(List.of(purchaseDTO), result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindPendingByUserId_Found() {
        when(purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING))
                .thenReturn(Optional.of(purchase));
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.findPendingByUserId(userId);

        assertEquals(purchaseDTO, result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindPendingByUserId_NotFound() {
        when(purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.findPendingByUserId(userId));
    }

    @Test
    void testAddGameToCart_ExistingCart() {
        Game game = new Game();
        game.setName("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));

        Inventory inventory = new Inventory();
        inventory.setGame(game);
        inventory.setStock(5);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(inventoryRepository.findByGameId(game.getId())).thenReturn(Optional.of(inventory));
        when(purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING))
                .thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.addGameToCart(userId, "Test Game");

        assertEquals(purchaseDTO, result);
        assertEquals(1, purchase.getPurchaseLines().size());
        assertEquals(BigDecimal.valueOf(19.99), purchase.getTotalPrice());
        assertEquals(4, inventory.getStock());
        verify(authorizationService).checkIsAdminOrOwner(userId);
        verify(inventoryRepository).save(inventory);
        verify(purchaseRepository).save(purchase);
    }

    @Test
    void testAddGameToCart_NewCart() {
        Game game = new Game();
        game.setName("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));

        Inventory inventory = new Inventory();
        inventory.setGame(game);
        inventory.setStock(5);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(inventoryRepository.findByGameId(game.getId())).thenReturn(Optional.of(inventory));
        when(purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING))
                .thenReturn(Optional.empty());
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(purchaseMapper.toDTO(any(Purchase.class))).thenReturn(purchaseDTO);

        PurchaseDTO result = purchaseService.addGameToCart(userId, "Test Game");

        assertEquals(purchaseDTO, result);
        verify(purchaseRepository, times(2)).save(any(Purchase.class));
    }

    @Test
    void testAddGameToCart_UserNotFound() {
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.addGameToCart(userId, "Test Game"));
    }

    @Test
    void testAddGameToCart_GameNotFound() {
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findByName("Unknown Game")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.addGameToCart(userId, "Unknown Game"));
    }

    @Test
    void testAddGameToCart_InventoryNotFound() {
        Game game = new Game();
        game.setName("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(inventoryRepository.findByGameId(game.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.addGameToCart(userId, "Test Game"));
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testAddGameToCart_OutOfStock() {
        Game game = new Game();
        game.setName("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));

        Inventory inventory = new Inventory();
        inventory.setGame(game);
        inventory.setStock(0);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(game));
        when(inventoryRepository.findByGameId(game.getId())).thenReturn(Optional.of(inventory));

        assertThrows(OutOfStockException.class, () -> purchaseService.addGameToCart(userId, "Test Game"));
        verify(inventoryRepository, never()).save(any());
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    void testUpdateStatus() {
        when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));
        when(purchaseRepository.save(purchase)).thenReturn(purchase);
        when(purchaseMapper.toDTO(purchase)).thenReturn(purchaseDTO);

        purchaseService.updateStatus(id, PurchaseStatus.PAID);

        assertEquals(PurchaseStatus.PAID, purchase.getStatus());
        verify(purchaseRepository).save(purchase);
    }

    @Test
    void testUpdateStatus_NotFound() {
        when(purchaseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.updateStatus(id, PurchaseStatus.PAID));
    }

    @Test
    void testDeleteById_Success() {
        when(purchaseRepository.existsById(id)).thenReturn(true);

        purchaseService.deleteById(id);

        verify(purchaseRepository).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        when(purchaseRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.deleteById(id));
        verify(purchaseRepository, never()).deleteById(id);
    }
}
