package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthorDTO {

    private UUID id;

    @NotBlank(message = "Un auteur doit avoir un nom.")
    private String name;

    private List<GameDTO> games;
}
