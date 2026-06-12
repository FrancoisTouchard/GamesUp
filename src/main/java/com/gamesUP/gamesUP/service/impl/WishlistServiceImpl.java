package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.WishlistDTO;
import com.gamesUP.gamesUP.exception.ResourceAlreadyExistsException;
import com.gamesUP.gamesUP.exception.ResourceNotFoundException;
import com.gamesUP.gamesUP.model.AppUser;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.AppUserRepository;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.WishlistService;
import com.gamesUP.gamesUP.service.mapper.WishlistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public List<WishlistDTO> findAll() {
        return wishlistRepository.findAll().stream()
                .map(wishlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WishlistDTO findById(UUID id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable"));

        authorizationService.checkIsAdminOrOwner(wishlist.getUser().getId());

        return wishlistMapper.toDTO(wishlist);
    }

    @Override
    public WishlistDTO findByUserId(UUID userId) {
        authorizationService.checkIsAdminOrOwner(userId);

        return wishlistRepository.findByUserId(userId)
                .map(wishlistMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable pour l'utilisateur : " + userId));
    }

    @Override
    @Transactional
    public WishlistDTO create(WishlistDTO wishlist) {
        if (wishlistRepository.existsByUserId(wishlist.getUserId())) {
            authorizationService.checkIsAdminOrOwner(wishlist.getUserId());
            throw new ResourceAlreadyExistsException("Une wishlist existe déjà pour cet utilisateur");
        }
        Wishlist wishlistToSave = wishlistMapper.toEntity(wishlist);

        return wishlistMapper.toDTO(wishlistRepository.save(wishlistToSave));
    }

    @Override
    @Transactional
    public WishlistDTO update(UUID id, WishlistDTO wishlist) {
        Wishlist existing = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable"));

        authorizationService.checkIsAdminOrOwner(existing.getUser().getId());

        if (wishlist.getGameNames() != null) {
            List<Game> games = wishlist.getGameNames().stream()
                    .map(name -> gameRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setWishlistedGames(games);
        }
        return wishlistMapper.toDTO(wishlistRepository.save(existing));
    }

    @Override
    @Transactional
    public WishlistDTO partialUpdate(UUID id, WishlistDTO wishlist) {
        Wishlist existing = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable"));

        authorizationService.checkIsAdminOrOwner(existing.getUser().getId());

        if (wishlist.getGameNames() != null) {
            List<Game> games = wishlist.getGameNames().stream()
                    .map(name -> gameRepository.findByName(name)
                            .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable : " + name)))
                    .collect(Collectors.toList());
            existing.setWishlistedGames(games);
        }
        return wishlistMapper.toDTO(wishlistRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist introuvable"));

        authorizationService.checkIsAdminOrOwner(wishlist.getUser().getId());

        AppUser user = wishlist.getUser();
        if (user != null) {
            user.setWishlist(null);
            appUserRepository.save(user);
        }

        wishlistRepository.deleteById(id);
    }
}
