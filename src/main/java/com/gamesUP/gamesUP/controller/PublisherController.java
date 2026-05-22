package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.PublisherDTO;
import com.gamesUP.gamesUP.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @Operation(summary = "Get all publishers")
    @GetMapping
    public List<PublisherDTO> findAll() {
        return publisherService.findAll();
    }

    @Operation(summary = "Get a publisher by id")
    @GetMapping("/{id}")
    public PublisherDTO findById(@PathVariable UUID id) {
        return publisherService.findById(id);
    }

    @Operation(summary = "Create a new publisher")
    @PostMapping
    public ResponseEntity<PublisherDTO> create(@Valid @RequestBody PublisherDTO publisher) {
        PublisherDTO created = publisherService.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a publisher")
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> update(@PathVariable UUID id, @Valid @RequestBody PublisherDTO publisher) {
        return ResponseEntity.ok(publisherService.update(id, publisher));
    }

    @Operation(summary = "Partially update a publisher")
    @PatchMapping("/{id}")
    public ResponseEntity<PublisherDTO> partialUpdate(@PathVariable UUID id, @RequestBody PublisherDTO publisher) {
        return ResponseEntity.ok(publisherService.partialUpdate(id, publisher));
    }

    @Operation(summary = "Delete a publisher by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        publisherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
