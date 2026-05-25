package com.gamesUP.gamesUP.service.mapper;

import com.gamesUP.gamesUP.dto.WishlistDTO;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    public WishlistDTO toDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());
        if (wishlist.getUser() != null) {
            dto.setUserId(wishlist.getUser().getId());
        }
        if (wishlist.getWishlistedGames() != null) {
            dto.setGameNames(wishlist.getWishlistedGames().stream()
                    .map(Game::getName)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public Wishlist toEntity(WishlistDTO dto) {
        Wishlist wishlist = new Wishlist();
        AppUser user = appUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + dto.getUserId()));
        wishlist.setUser(user);
        if (dto.getGameNames() != null) {
            List<Game> games = dto.getGameNames().stream()
                    .map(name -> gameRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + name)))
                    .collect(Collectors.toList());
            wishlist.setWishlistedGames(games);
        } else {
            wishlist.setWishlistedGames(Collections.emptyList());
        }
        return wishlist;
    }
}
