package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.recommendation.RecommendedGameDTO;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<RecommendedGameDTO> getRecommendationsForUser(UUID userId);
}
