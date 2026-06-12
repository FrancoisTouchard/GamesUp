package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories/private")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping("/user")
    public List<CategoryDTO> findAll(){
        return categoryService.findAll();
    }

    @Operation(summary = "Get a category by id")
    @GetMapping("/user/{id}")
    public CategoryDTO findById(@PathVariable UUID id){
        return categoryService.findById(id);
    }

    @Operation(summary = "Create a new category")
    @PostMapping("/admin")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO created = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update a category")
    @PutMapping("/admin/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable UUID id, @Valid @RequestBody CategoryDTO category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    @Operation(summary = "Partially update a category")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<CategoryDTO> partialUpdate(@PathVariable UUID id, @RequestBody CategoryDTO category) {
        return ResponseEntity.ok(categoryService.partialUpdate(id, category));
    }

    @Operation(summary = "Delete a category by id")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
