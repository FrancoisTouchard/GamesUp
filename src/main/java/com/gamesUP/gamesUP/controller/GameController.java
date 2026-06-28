package com.gamesUP.gamesUP.controller;

import java.util.List;
import java.util.UUID;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/games/private")
public class GameController {

    @Autowired
    private GameService gameService;

    @Operation(summary = "Get all games")
    @GetMapping("/user")
    public List<GameDTO> findAll(){
        return gameService.findAll();
    }

    @Operation(summary = "Get a game by id")
    @GetMapping("/user/{id}")
    public GameDTO findById(@PathVariable UUID id){
        return gameService.findById(id);
    }

    @Operation(summary = "Get a game by name")
    @GetMapping(value = "/user", params = "name")
    public GameDTO findByName(@RequestParam String name){ return gameService.findByName(name);}

    @Operation(summary = "Get all games from a category by name")
    @GetMapping("/user/category/{categoryName}")
    public List<GameDTO> findByCategoryName(@PathVariable String categoryName) {
        return gameService.findByCategoryName(categoryName);
    }

    @Operation(summary = "Get all games from a genre by name")
    @GetMapping("/user/genre/{genreName}")
    public List<GameDTO> findByGenreName(@PathVariable String genreName) {
        return gameService.findByGenreName(genreName);
    }

    @Operation(summary = "Get all games from a publisher by name")
    @GetMapping("/user/publisher/{publisherName}")
    public List<GameDTO> findByPublisherName(@PathVariable String publisherName) {
        return gameService.findByPublisherName(publisherName);
    }

    @Operation(summary = "Get all games from an author by name")
    @GetMapping("/user/author/{authorName}")
    public List<GameDTO> findByAuthorName(@PathVariable String authorName) {
        return gameService.findByAuthorName(authorName);
    }

    @Operation(summary = "Create a new game")
    @PostMapping("/admin")
    public ResponseEntity<GameDTO> create(@Valid @RequestBody GameDTO game) {
        GameDTO created = gameService.create(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a game")
    @PutMapping("/admin/{id}")
    public ResponseEntity<GameDTO> update(@PathVariable UUID id, @Valid @RequestBody GameDTO game) {
        return ResponseEntity.ok(gameService.update(id, game));
    }

    @Operation(summary = "Partially update a game")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<GameDTO> partialUpdate(@PathVariable UUID id, @RequestBody GameDTO game) {
        return ResponseEntity.ok(gameService.partialUpdate(id, game));
    }

    @Operation(summary = "Delete a game by id")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        gameService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}