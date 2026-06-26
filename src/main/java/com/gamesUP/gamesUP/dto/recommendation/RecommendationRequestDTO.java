package com.gamesUP.gamesUP.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RecommendationRequestDTO {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("purchased_game_ids")
    private List<UUID> purchasedGameIds;

    @JsonProperty("wishlisted_game_ids")
    private List<UUID> wishlistedGameIds;

    private List<GameFeatureDTO> catalog;

    @JsonProperty("top_n")
    private int topN;
}
