package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByGameId(UUID gameId);
    boolean existsByGameId(UUID gameId);
}
