package com.gamesUP.gamesUP.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RecommendationResponseDTO {

    @JsonProperty("user_id")
    private UUID userId;

    private List<RecommendedGameDTO> recommendations;
}
