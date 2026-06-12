package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.PurchaseLineDTO;
import com.gamesUP.gamesUP.service.PurchaseLineService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchase-lines/private/user")
public class PurchaseLineController {

    @Autowired
    private PurchaseLineService purchaseLineService;

    @Operation(summary = "Get a purchase line by id")
    @GetMapping("/{id}")
    public PurchaseLineDTO findById(@PathVariable UUID id) {
        return purchaseLineService.findById(id);
    }

    @Operation(summary = "Get all purchase lines by purchase id")
    @GetMapping("/purchase/{purchaseId}")
    public List<PurchaseLineDTO> findByPurchaseId(@PathVariable UUID purchaseId) {
        return purchaseLineService.findByPurchaseId(purchaseId);
    }

    @Operation(summary = "Remove a game from the cart")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        purchaseLineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
