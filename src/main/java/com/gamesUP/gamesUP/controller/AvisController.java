package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.AvisDTO;
import com.gamesUP.gamesUP.service.AvisService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avis/private")
public class AvisController {

    @Autowired
    private AvisService avisService;

    @Operation(summary = "Get all avis")
    @GetMapping("/admin")
    public List<AvisDTO> findAll() {
        return avisService.findAll();
    }

    @Operation(summary = "Get an avis by id")
    @GetMapping("/user/{id}")
    public AvisDTO findById(@PathVariable UUID id) {
        return avisService.findById(id);
    }

    @Operation(summary = "Get all avis by user id")
    @GetMapping("/user/byUser/{userId}")
    public List<AvisDTO> findByUserId(@PathVariable UUID userId) {
        return avisService.findByUserId(userId);
    }

    @Operation(summary = "Get all avis by game id")
    @GetMapping("/user/byGame/{gameId}")
    public List<AvisDTO> findByGameId(@PathVariable UUID gameId) {
        return avisService.findByGameId(gameId);
    }

    @Operation(summary = "Create a new avis")
    @PostMapping("/user")
    public ResponseEntity<AvisDTO> create(@Valid @RequestBody AvisDTO avis) {
        AvisDTO created = avisService.create(avis);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update an avis")
    @PutMapping("/user/{id}")
    public ResponseEntity<AvisDTO> update(@PathVariable UUID id, @Valid @RequestBody AvisDTO avis) {
        return ResponseEntity.ok(avisService.update(id, avis));
    }

    @Operation(summary = "Partially update an avis")
    @PatchMapping("/user/{id}")
    public ResponseEntity<AvisDTO> partialUpdate(@PathVariable UUID id, @RequestBody AvisDTO avis) {
        return ResponseEntity.ok(avisService.partialUpdate(id, avis));
    }

    @Operation(summary = "Delete an avis by id")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        avisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
