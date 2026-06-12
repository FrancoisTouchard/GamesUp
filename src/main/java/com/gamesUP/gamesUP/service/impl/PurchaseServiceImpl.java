package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.exception.OutOfStockException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.InventoryRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.PurchaseService;
import com.gamesUP.gamesUP.service.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public List<PurchaseDTO> findAll() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDTO findById(UUID id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + id));

        authorizationService.checkIsAdminOrOwner(purchase.getUser().getId());

        return purchaseMapper.toDTO(purchase);
    }

    @Override
    public List<PurchaseDTO> findByUserId(UUID userId) {
        authorizationService.checkIsAdminOrOwner(userId);

        return purchaseRepository.findByUserId(userId).stream()
                .map(purchaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDTO findPendingByUserId(UUID userId) {
        authorizationService.checkIsAdminOrOwner(userId);

        return purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING)
                .map(purchaseMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun panier en cours pour l'utilisateur : " + userId));
    }

    @Override
    @Transactional
    public PurchaseDTO addGameToCart(UUID userId, String gameName) {
        authorizationService.checkIsAdminOrOwner(userId);

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + userId));

        Game game = gameRepository.findByName(gameName)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + gameName));

        Inventory inventory = inventoryRepository.findByGameId(game.getId())
                .orElseThrow(() -> new ResourceNotFoundException(gameName + " n'est pas référencé dans l'inventaire du magasin."));

        if (inventory.getStock() <= 0) {
            throw new OutOfStockException(gameName + " est en rupture de stock.");
        }

        inventory.setStock(inventory.getStock() - 1);
        inventoryRepository.save(inventory);

        Purchase purchase = purchaseRepository.findByUserIdAndStatus(userId, PurchaseStatus.PENDING)
                .orElseGet(() -> createNewCart(user));

        PurchaseLine line = new PurchaseLine();
        line.setPurchase(purchase);
        line.setGame(game);
        purchase.getPurchaseLines().add(line);

        recalculateTotalPrice(purchase);
        return purchaseMapper.toDTO(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public PurchaseDTO updateStatus(UUID id, PurchaseStatus status) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + id));
        purchase.setStatus(status);
        return purchaseMapper.toDTO(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!purchaseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commande introuvable : " + id);
        }
        purchaseRepository.deleteById(id);
    }

    private Purchase createNewCart(AppUser user) {
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setStatus(PurchaseStatus.PENDING);
        purchase.setDate(LocalDateTime.now());
        purchase.setTotalPrice(BigDecimal.ZERO);
        purchase.setPurchaseLines(new ArrayList<>());
        return purchaseRepository.save(purchase);
    }

    private void recalculateTotalPrice(Purchase purchase) {
        BigDecimal total = purchase.getPurchaseLines().stream()
                .map(line -> line.getGame().getPrice() != null ? line.getGame().getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        purchase.setTotalPrice(total);
    }
}
