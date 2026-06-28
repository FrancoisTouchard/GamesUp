package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.AvisDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvisMapper {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    public AvisDTO toDTO(Avis avis) {
        AvisDTO dto = new AvisDTO();
        dto.setId(avis.getId());
        dto.setComment(avis.getComment());
        dto.setRating(avis.getRating());
        if (avis.getUser() != null) {
            dto.setUserId(avis.getUser().getId());
        }
        if (avis.getGame() != null) {
            dto.setGameName(avis.getGame().getName());
        }
        return dto;
    }

    public Avis toEntity(AvisDTO dto) {
        Avis avis = new Avis();
        avis.setComment(dto.getComment());
        avis.setRating(dto.getRating());
        AppUser user = appUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + dto.getUserId()));
        avis.setUser(user);
        Game game = gameRepository.findByName(dto.getGameName())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + dto.getGameName()));
        avis.setGame(game);
        return avis;
    }
}
