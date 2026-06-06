package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseLine;
import com.gamesUP.gamesUP.repository.PurchaseLineRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
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

    @Override
    public PurchaseLineDTO findById(UUID id) {
        return purchaseLineRepository.findById(id)
                .map(purchaseLineMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne de commande introuvable : " + id));
    }

    @Override
    public List<PurchaseLineDTO> findByPurchaseId(UUID purchaseId) {
        return purchaseLineRepository.findByPurchaseId(purchaseId).stream()
                .map(purchaseLineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        PurchaseLine line = purchaseLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne de commande introuvable : " + id));

        Purchase purchase = line.getPurchase();
        purchase.getPurchaseLines().remove(line);

        BigDecimal total = purchase.getPurchaseLines().stream()
                .map(l -> l.getGame().getPrice() != null ? l.getGame().getPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        purchase.setTotalPrice(total);
        purchaseRepository.save(purchase);
    }
}
