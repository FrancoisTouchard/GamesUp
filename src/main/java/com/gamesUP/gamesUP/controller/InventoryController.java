package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.InventoryDTO;
import com.gamesUP.gamesUP.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventories/private")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "Get all inventory entries")
    @GetMapping("/user")
    public List<InventoryDTO> findAll() {
        return inventoryService.findAll();
    }

    @Operation(summary = "Get an inventory entry by id")
    @GetMapping("/user/{id}")
    public InventoryDTO findById(@PathVariable UUID id) {
        return inventoryService.findById(id);
    }

    @Operation(summary = "Get the inventory entry of a game by id")
    @GetMapping("/user/game/{gameId}")
    public InventoryDTO findByGameId(@PathVariable UUID gameId) {
        return inventoryService.findByGameId(gameId);
    }

    @Operation(summary = "Create a new inventory entry")
    @PostMapping("/admin")
    public ResponseEntity<InventoryDTO> create(@Valid @RequestBody InventoryDTO inventory) {
        InventoryDTO created = inventoryService.create(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Partially update an inventory entry")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<InventoryDTO> partialUpdate(@PathVariable UUID id, @RequestBody InventoryDTO inventory) {
        return ResponseEntity.ok(inventoryService.partialUpdate(id, inventory));
    }

    @Operation(summary = "Delete an inventory entry by id")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        inventoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
