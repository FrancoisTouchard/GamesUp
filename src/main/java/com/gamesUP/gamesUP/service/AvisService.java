package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.AvisDTO;

import java.util.List;
import java.util.UUID;

public interface AvisService {
    List<AvisDTO> findAll();
    AvisDTO findById(UUID id);
    List<AvisDTO> findByUserId(UUID userId);
    List<AvisDTO> findByGameId(UUID gameId);
    AvisDTO create(AvisDTO avis);
    AvisDTO update(UUID id, AvisDTO avis);
    AvisDTO partialUpdate(UUID id, AvisDTO avis);
    void deleteById(UUID id);
}
