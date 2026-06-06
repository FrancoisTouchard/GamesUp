package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseLineMapper {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public PurchaseLineDTO toDTO(PurchaseLine purchaseLine) {
        PurchaseLineDTO dto = new PurchaseLineDTO();
        dto.setId(purchaseLine.getId());
        if (purchaseLine.getPurchase() != null) {
            dto.setPurchaseId(purchaseLine.getPurchase().getId());
        }
        if (purchaseLine.getGame() != null) {
            dto.setGameName(purchaseLine.getGame().getName());
        }
        return dto;
    }

    public PurchaseLine toEntity(PurchaseLineDTO dto) {
        PurchaseLine purchaseLine = new PurchaseLine();
        Purchase purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + dto.getPurchaseId()));
        purchaseLine.setPurchase(purchase);
        Game game = gameRepository.findByName(dto.getGameName())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + dto.getGameName()));
        purchaseLine.setGame(game);
        return purchaseLine;
    }
}
