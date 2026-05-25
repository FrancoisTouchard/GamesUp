package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.AppUserDTO;
import com.gamesUP.gamesUP.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<AppUserDTO> findAll() {
        return appUserService.findAll();
    }

    @Operation(summary = "Get a user by id")
    @GetMapping("/{id}")
    public AppUserDTO findById(@PathVariable UUID id) {
        return appUserService.findById(id);
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<AppUserDTO> create(@Valid @RequestBody AppUserDTO user) {
        AppUserDTO created = appUserService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> update(@PathVariable UUID id, @Valid @RequestBody AppUserDTO user) {
        return ResponseEntity.ok(appUserService.update(id, user));
    }

    @Operation(summary = "Partially update a user")
    @PatchMapping("/{id}")
    public ResponseEntity<AppUserDTO> partialUpdate(@PathVariable UUID id, @RequestBody AppUserDTO user) {
        return ResponseEntity.ok(appUserService.partialUpdate(id, user));
    }

    @Operation(summary = "Delete a user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        appUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
