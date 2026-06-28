package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.AvisDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.AvisRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.AvisService;
import com.gamesUP.gamesUP.service.mapper.AvisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvisServiceImpl implements AvisService {

    @Autowired
    private AvisRepository avisRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AvisMapper avisMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public List<AvisDTO> findAll() {
        return avisRepository.findAll().stream()
                .map(avisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AvisDTO findById(UUID id) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        authorizationService.checkIsAdminOrOwner(avis.getUser().getId());
        return avisMapper.toDTO(avis);
    }

    @Override
    public List<AvisDTO> findByUserId(UUID userId) {
        authorizationService.checkIsAdminOrOwner(userId);
        return avisRepository.findByUserId(userId).stream()
                .map(avisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AvisDTO> findByGameId(UUID gameId) {
        return avisRepository.findByGameId(gameId).stream()
                .map(avisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvisDTO create(AvisDTO avis) {
        authorizationService.checkIsAdminOrOwner(avis.getUserId());
        Avis avisToSave = avisMapper.toEntity(avis);
        return avisMapper.toDTO(avisRepository.save(avisToSave));
    }

    @Override
    @Transactional
    public AvisDTO update(UUID id, AvisDTO avis) {
        Avis existing = avisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        authorizationService.checkIsAdminOrOwner(existing.getUser().getId());
        existing.setComment(avis.getComment());
        existing.setRating(avis.getRating());
        Game game = gameRepository.findByName(avis.getGameName())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + avis.getGameName()));
        existing.setGame(game);
        return avisMapper.toDTO(avisRepository.save(existing));
    }

    @Override
    @Transactional
    public AvisDTO partialUpdate(UUID id, AvisDTO avis) {
        Avis existing = avisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        authorizationService.checkIsAdminOrOwner(existing.getUser().getId());
        if (avis.getComment() != null) existing.setComment(avis.getComment());
        if (avis.getRating() != null) existing.setRating(avis.getRating());
        if (avis.getGameName() != null) {
            Game game = gameRepository.findByName(avis.getGameName())
                    .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + avis.getGameName()));
            existing.setGame(game);
        }
        return avisMapper.toDTO(avisRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        authorizationService.checkIsAdminOrOwner(avis.getUser().getId());
        avisRepository.deleteById(id);
    }
}
