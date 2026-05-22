package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameDTO {

    private UUID id;

    @NotBlank(message = "Un jeu doit avoir un nom.")
    private String name;

    private List<String> authorNames;

    private String genre;
    private String categoryName;

    @NotNull(message = "Un jeu doit avoir un éditeur.")
    private String publisherName;

    private int numEdition;

}
