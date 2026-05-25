package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.WishlistDTO;
import com.gamesUP.gamesUP.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlists")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Operation(summary = "Get all wishlists")
    @GetMapping
    public List<WishlistDTO> findAll() {
        return wishlistService.findAll();
    }

    @Operation(summary = "Get a wishlist by id")
    @GetMapping("/{id}")
    public WishlistDTO findById(@PathVariable UUID id) {
        return wishlistService.findById(id);
    }

    @Operation(summary = "Get a wishlist by user id")
    @GetMapping("/user/{userId}")
    public WishlistDTO findByUserId(@PathVariable UUID userId) {
        return wishlistService.findByUserId(userId);
    }

    @Operation(summary = "Create a new wishlist")
    @PostMapping
    public ResponseEntity<WishlistDTO> create(@Valid @RequestBody WishlistDTO wishlist) {
        WishlistDTO created = wishlistService.create(wishlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a wishlist")
    @PutMapping("/{id}")
    public ResponseEntity<WishlistDTO> update(@PathVariable UUID id, @Valid @RequestBody WishlistDTO wishlist) {
        return ResponseEntity.ok(wishlistService.update(id, wishlist));
    }

    @Operation(summary = "Partially update a wishlist")
    @PatchMapping("/{id}")
    public ResponseEntity<WishlistDTO> partialUpdate(@PathVariable UUID id, @RequestBody WishlistDTO wishlist) {
        return ResponseEntity.ok(wishlistService.partialUpdate(id, wishlist));
    }

    @Operation(summary = "Delete a wishlist by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        wishlistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
