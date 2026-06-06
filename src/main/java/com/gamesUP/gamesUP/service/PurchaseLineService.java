package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;

import java.util.List;
import java.util.UUID;

public interface PurchaseLineService {
    PurchaseLineDTO findById(UUID id);

    List<PurchaseLineDTO> findByPurchaseId(UUID purchaseId);

    void deleteById(UUID id);
}
