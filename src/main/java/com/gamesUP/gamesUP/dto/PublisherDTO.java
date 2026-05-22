package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PublisherDTO {

    private UUID id;

    @NotBlank(message = "Un éditeur doit avoir un nom.")
    private String name;

    private List<GameDTO> games;
}
