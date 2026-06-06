package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.PurchaseLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, UUID> {

    List<PurchaseLine> findByPurchaseId(UUID purchaseId);
    boolean existsByGameId(UUID gameId);
}
