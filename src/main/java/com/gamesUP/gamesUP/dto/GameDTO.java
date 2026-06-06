package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameDTO {

    private UUID id;

    @NotBlank(message = "Un jeu doit avoir un nom.")
    private String name;

    private List<String> authorNames;

    private List<String> genreNames;
    private List<String> categoryNames;

    @NotNull(message = "Un jeu doit avoir un éditeur.")
    private String publisherName;

    private int numEdition;

    @Positive(message = "Le prix doit être positif.")
    private BigDecimal price;

}
