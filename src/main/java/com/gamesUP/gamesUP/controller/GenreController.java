package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.GenreDTO;
import com.gamesUP.gamesUP.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres/private")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Operation(summary = "Get all genres")
    @GetMapping("/user")
    public List<GenreDTO> findAll() {
        return genreService.findAll();
    }

    @Operation(summary = "Get a genre by id")
    @GetMapping("/user/{id}")
    public GenreDTO findById(@PathVariable UUID id) {
        return genreService.findById(id);
    }

    @Operation(summary = "Create a new genre")
    @PostMapping("/admin")
    public ResponseEntity<GenreDTO> create(@Valid @RequestBody GenreDTO genre) {
        GenreDTO created = genreService.create(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a genre")
    @PutMapping("/admin/{id}")
    public ResponseEntity<GenreDTO> update(@PathVariable UUID id, @Valid @RequestBody GenreDTO genre) {
        return ResponseEntity.ok(genreService.update(id, genre));
    }

    @Operation(summary = "Partially update a genre")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<GenreDTO> partialUpdate(@PathVariable UUID id, @RequestBody GenreDTO genre) {
        return ResponseEntity.ok(genreService.partialUpdate(id, genre));
    }

    @Operation(summary = "Delete a genre by id")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        genreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
