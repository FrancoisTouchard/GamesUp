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

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.mapper.PurchaseLineMapper;

class PurchaseLineServiceImplTest {

    @Mock
    private PurchaseLineRepository purchaseLineRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseLineMapper purchaseLineMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private PurchaseLineServiceImpl purchaseLineService;

    private Purchase purchase;
    private PurchaseLine purchaseLine;
    private PurchaseLineDTO purchaseLineDTO;
    private AppUser user;
    private UUID id;
    private UUID purchaseId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        purchaseId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new AppUser();
        user.setId(userId);

        Game game = new Game();
        game.setName("Test Game");
        game.setPrice(BigDecimal.valueOf(19.99));

        purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUser(user);
        purchase.setPurchaseLines(new ArrayList<>());

        purchaseLine = new PurchaseLine();
        purchaseLine.setId(id);
        purchaseLine.setPurchase(purchase);
        purchaseLine.setGame(game);
        purchase.getPurchaseLines().add(purchaseLine);

        purchaseLineDTO = new PurchaseLineDTO();
        purchaseLineDTO.setId(id);
        purchaseLineDTO.setPurchaseId(purchaseId);
        purchaseLineDTO.setGameName("Test Game");
    }

    @Test
    void testFindById_Found() {
        when(purchaseLineRepository.findById(id)).thenReturn(Optional.of(purchaseLine));
        when(purchaseLineMapper.toDTO(purchaseLine)).thenReturn(purchaseLineDTO);

        PurchaseLineDTO result = purchaseLineService.findById(id);

        assertEquals(purchaseLineDTO, result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindById_NotFound() {
        when(purchaseLineRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseLineService.findById(id));
    }

    @Test
    void testFindByPurchaseId() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));
        when(purchaseLineRepository.findByPurchaseId(purchaseId)).thenReturn(List.of(purchaseLine));
        when(purchaseLineMapper.toDTO(purchaseLine)).thenReturn(purchaseLineDTO);

        List<PurchaseLineDTO> result = purchaseLineService.findByPurchaseId(purchaseId);

        assertEquals(List.of(purchaseLineDTO), result);
        verify(authorizationService).checkIsAdminOrOwner(userId);
    }

    @Test
    void testFindByPurchaseId_NotFound() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseLineService.findByPurchaseId(purchaseId));
    }

    @Test
    void testDeleteById_Success() {
        when(purchaseLineRepository.findById(id)).thenReturn(Optional.of(purchaseLine));

        purchaseLineService.deleteById(id);

        assertTrue(purchase.getPurchaseLines().isEmpty());
        assertEquals(BigDecimal.ZERO, purchase.getTotalPrice());
        verify(authorizationService).checkIsAdminOrOwner(userId);
        verify(purchaseRepository).save(purchase);
    }

    @Test
    void testDeleteById_NotFound() {
        when(purchaseLineRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseLineService.deleteById(id));
        verify(purchaseRepository, never()).save(any());
    }
}
