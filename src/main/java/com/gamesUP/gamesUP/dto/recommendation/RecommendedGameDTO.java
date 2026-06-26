package com.gamesUP.gamesUP.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RecommendedGameDTO {

    @JsonProperty("game_id")
    private UUID gameId;

    @JsonProperty("game_name")
    private String gameName;

    private double score;
}
