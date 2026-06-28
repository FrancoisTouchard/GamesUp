package com.gamesUP.gamesUP.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AvisDTO {

    private UUID id;

    private UUID userId;

    private String gameName;

    private String comment;

    private Integer rating;
}
