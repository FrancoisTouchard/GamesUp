package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.PurchaseDTO;
import com.gamesUP.gamesUP.model.PurchaseStatus;
import com.gamesUP.gamesUP.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Operation(summary = "Get all purchases")
    @GetMapping
    public List<PurchaseDTO> findAll() {
        return purchaseService.findAll();
    }

    @Operation(summary = "Get a purchase by id")
    @GetMapping("/{id}")
    public PurchaseDTO findById(@PathVariable UUID id) {
        return purchaseService.findById(id);
    }

    @Operation(summary = "Get all purchases of a user by user id")
    @GetMapping("/user/{userId}")
    public List<PurchaseDTO> findByUserId(@PathVariable UUID userId) {
        return purchaseService.findByUserId(userId);
    }

    @Operation(summary = "Get the pending cart of a user")
    @GetMapping("/user/{userId}/pending")
    public PurchaseDTO findPendingByUserId(@PathVariable UUID userId) {
        return purchaseService.findPendingByUserId(userId);
    }

    @Operation(summary = "Add a game to the user's cart")
    @PostMapping("/user/{userId}/cart")
    public ResponseEntity<PurchaseDTO> addGameToCart(@PathVariable UUID userId, @RequestParam String gameName) {
        PurchaseDTO purchase = purchaseService.addGameToCart(userId, gameName);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    @Operation(summary = "Update the status of a purchase")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseDTO> updateStatus(@PathVariable UUID id, @RequestParam PurchaseStatus status) {
        return ResponseEntity.ok(purchaseService.updateStatus(id, status));
    }

    @Operation(summary = "Delete a purchase by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        purchaseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
