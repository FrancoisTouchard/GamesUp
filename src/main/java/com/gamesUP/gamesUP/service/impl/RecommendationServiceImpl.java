package com.gamesUP.gamesUP.service.impl;

import com.gamesUP.gamesUP.dto.recommendation.GameFeatureDTO;
import com.gamesUP.gamesUP.dto.recommendation.RecommendationRequestDTO;
import com.gamesUP.gamesUP.dto.recommendation.RecommendedGameDTO;
import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Genre;
import com.gamesUP.gamesUP.model.PurchaseStatus;
import com.gamesUP.gamesUP.repository.GameRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import com.gamesUP.gamesUP.service.AuthorizationService;
import com.gamesUP.gamesUP.service.RecommendationService;
import com.gamesUP.gamesUP.service.client.RecommendationApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final int TOP_N = 5;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RecommendationApiClient recommendationApiClient;

    @Override
    public List<RecommendedGameDTO> getRecommendationsForUser(UUID userId) {
        authorizationService.checkIsAdminOrOwner(userId);
        // Construire la requête contenant toutes les infos pour l'algo de ML
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setUserId(userId);
        request.setPurchasedGameIds(findPurchasedGameIds(userId));
        request.setWishlistedGameIds(findWishlistedGameIds(userId));
        request.setCatalog(buildCatalog());
        request.setTopN(TOP_N);

        return recommendationApiClient.fetchRecommendations(request);
    }

    private List<GameFeatureDTO> buildCatalog() {
        return gameRepository.findAll().stream()
                .map(this::toFeatureDTO)
                .collect(Collectors.toList());
    }

    private GameFeatureDTO toFeatureDTO(Game game) {
        GameFeatureDTO dto = new GameFeatureDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setGenreNames(mapToList(game.getGenres(), Genre::getName));
        dto.setCategoryNames(mapToList(game.getCategories(), Category::getName));
        dto.setAuthorNames(mapToList(game.getAuthors(), Author::getName));
        dto.setPublisherName(game.getPublisher() != null ? game.getPublisher().getName() : null);
        return dto;
    }

    private <T, R> List<R> mapToList(List<T> entities, Function<T, R> extractor) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(extractor).collect(Collectors.toList());
    }

    private List<UUID> findPurchasedGameIds(UUID userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .flatMap(purchase -> purchase.getPurchaseLines().stream())
                .map(line -> line.getGame().getId())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<UUID> findWishlistedGameIds(UUID userId) {
        return wishlistRepository.findByUserId(userId)
                .map(wishlist -> mapToList(wishlist.getWishlistedGames(), Game::getId))
                .map(ids -> ids.stream().distinct().collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
