package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AvisRepository extends JpaRepository<Avis, UUID> {

    List<Avis> findByUserId(UUID userId);
    List<Avis> findByGameId(UUID gameId);
}
