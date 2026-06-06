package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    List<Purchase> findByUserId(UUID userId);
    Optional<Purchase> findByUserIdAndStatus(UUID userId, PurchaseStatus status);
}
