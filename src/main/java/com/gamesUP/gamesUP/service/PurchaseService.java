package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.model.PurchaseStatus;

import java.util.List;
import java.util.UUID;

public interface PurchaseService {
    List<PurchaseDTO> findAll();

    PurchaseDTO findById(UUID id);

    List<PurchaseDTO> findByUserId(UUID userId);

    PurchaseDTO findPendingByUserId(UUID userId);

    PurchaseDTO addGameToCart(UUID userId, String gameName);

    PurchaseDTO updateStatus(UUID id, PurchaseStatus status);

    void deleteById(UUID id);
}
