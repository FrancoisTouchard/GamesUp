package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryDTO {

    private UUID id;

    @NotBlank(message = "Une catégorie doit avoir un nom.")
    private String name;
}
