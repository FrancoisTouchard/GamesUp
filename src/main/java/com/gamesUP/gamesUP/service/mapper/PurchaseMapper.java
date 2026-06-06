package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PurchaseMapper {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PurchaseLineMapper purchaseLineMapper;

    public PurchaseDTO toDTO(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(purchase.getId());
        if (purchase.getUser() != null) {
            dto.setUserId(purchase.getUser().getId());
        }
        dto.setTotalPrice(purchase.getTotalPrice());
        dto.setDate(purchase.getDate());
        dto.setStatus(purchase.getStatus());
        if (purchase.getPurchaseLines() != null) {
            dto.setPurchaseLines(purchase.getPurchaseLines().stream()
                    .map(purchaseLineMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public Purchase toEntity(PurchaseDTO dto) {
        Purchase purchase = new Purchase();
        AppUser user = appUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + dto.getUserId()));
        purchase.setUser(user);
        purchase.setTotalPrice(dto.getTotalPrice());
        purchase.setDate(dto.getDate());
        purchase.setStatus(dto.getStatus());
        purchase.setPurchaseLines(Collections.emptyList());
        return purchase;
    }
}
