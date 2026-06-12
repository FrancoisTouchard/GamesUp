package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.AuthorDTO;
import com.gamesUP.gamesUP.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors/private")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get all authors")
    @GetMapping("/user")
    public List<AuthorDTO> findAll() {
        return authorService.findAll();
    }

    @Operation(summary = "Get an author by id")
    @GetMapping("/user/{id}")
    public AuthorDTO findById(@PathVariable UUID id) {
        return authorService.findById(id);
    }

    @Operation(summary = "Create a new author")
    @PostMapping("/admin")
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorDTO author) {
        AuthorDTO created = authorService.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update an author")
    @PutMapping("/admin/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable UUID id, @Valid @RequestBody AuthorDTO author) {
        return ResponseEntity.ok(authorService.update(id, author));
    }

    @Operation(summary = "Partially update an author")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<AuthorDTO> partialUpdate(@PathVariable UUID id, @RequestBody AuthorDTO author) {
        return ResponseEntity.ok(authorService.partialUpdate(id, author));
    }

    @Operation(summary = "Delete an author by id")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
