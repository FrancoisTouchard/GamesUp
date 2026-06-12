package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.PurchaseLineService;
import com.gamesUP.gamesUP.service.mapper.PurchaseLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class PurchaseLineServiceImpl implements PurchaseLineService {

    @Autowired
    private PurchaseLineRepository purchaseLineRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseLineMapper purchaseLineMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public PurchaseLineDTO findById(UUID id) {
        PurchaseLine purchaseLine = purchaseLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne de commande introuvable : " + id));

        authorizationService.checkIsAdminOrOwner(purchaseLine.getPurchase().getUser().getId());

        return purchaseLineMapper.toDTO(purchaseLine);
    }

    @Override
    public List<PurchaseLineDTO> findByPurchaseId(UUID purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable : " + purchaseId));

        authorizationService.checkIsAdminOrOwner(purchase.getUser().getId());

        return purchaseLineRepository.findByPurchaseId(purchaseId).stream()
                .map(purchaseLineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        PurchaseLine purchaseLine = purchaseLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne de commande introuvable : " + id));

        authorizationService.checkIsAdminOrOwner(purchaseLine.getPurchase().getUser().getId());

        Purchase purchase = purchaseLine.getPurchase();
        purchase.getPurchaseLines().remove(purchaseLine);

        BigDecimal total = purchase.getPurchaseLines().stream()
                .map(l -> l.getGame().getPrice() != null ? l.getGame().getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        purchase.setTotalPrice(total);
        purchaseRepository.save(purchase);
    }
}
