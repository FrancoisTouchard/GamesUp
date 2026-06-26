package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.recommendation.RecommendedGameDTO;
import com.gamesUP.gamesUP.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendations/private")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Operation(summary = "Get game recommendations for a user")
    @GetMapping("/user/{userId}")
    public List<RecommendedGameDTO> getRecommendations(@PathVariable UUID userId) {
        return recommendationService.getRecommendationsForUser(userId);
    }
}
