package com.gamesUP.gamesUP.service.client;

import com.gamesUP.gamesUP.dto.recommendation.RecommendationRequestDTO;
import com.gamesUP.gamesUP.dto.recommendation.RecommendationResponseDTO;
import com.gamesUP.gamesUP.dto.recommendation.RecommendedGameDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;

@Component
public class RecommendationApiClient {

    private final RestClient restClient;

    public RecommendationApiClient(@Value("${recommendation.api.url}") String recommendationApiUrl) {
        // Fix : forcer la version HTTP_1_1 pour compatibilité avec uvicorn
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.restClient = RestClient.builder()
                .baseUrl(recommendationApiUrl)
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }

    public List<RecommendedGameDTO> fetchRecommendations(RecommendationRequestDTO request) {
        RecommendationResponseDTO response = restClient.post()
                .uri("/recommendations")
                .body(request)
                .retrieve()
                .body(RecommendationResponseDTO.class);

        return response != null && response.getRecommendations() != null
                ? response.getRecommendations()
                : Collections.emptyList();
    }
}
