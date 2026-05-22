package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GenreDTO {

    private UUID id;

    @NotBlank(message = "Un genre doit avoir un nom.")
    private String name;
}
