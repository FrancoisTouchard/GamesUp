package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.WishlistDTO;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    List<WishlistDTO> findAll();
    WishlistDTO findById(UUID id);
    WishlistDTO findByUserId(UUID userId);
    WishlistDTO create(WishlistDTO wishlist);
    WishlistDTO update(UUID id, WishlistDTO wishlist);
    WishlistDTO partialUpdate(UUID id, WishlistDTO wishlist);
    void deleteById(UUID id);
}
