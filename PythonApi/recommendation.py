from typing import List, Optional

import numpy as np
import pandas as pd
from sklearn.neighbors import NearestNeighbors

from data_loader import build_game_feature_matrix
from models import RecommendationRequest, RecommendedGame

PURCHASE_WEIGHT = 1.0
WISHLIST_WEIGHT = 0.75


def generate_recommendations(request: RecommendationRequest) -> List[RecommendedGame]:
    if not request.catalog:
        return []

    feature_matrix = build_game_feature_matrix(request.catalog)

    profile_vector = _build_user_profile(feature_matrix, request)
    if profile_vector is None:
        return []
    
    # Afficher le score avec le nom de la colonne associée
    # profile_series = pd.Series(profile_vector, index=feature_matrix.columns)
    # print(profile_series[profile_series > 0].sort_values(ascending=False).to_string())

    # Exclure les jeux déjà achetés ou wishlistés de la recommandation
    owned_ids = set(request.purchased_game_ids) | set(request.wishlisted_game_ids)
    candidate_ids = [game_id for game_id in feature_matrix.index if game_id not in owned_ids]
    if not candidate_ids:
        return []

    candidates = feature_matrix.loc[candidate_ids]
    n_neighbors = max(1, min(request.top_n, len(candidate_ids)))

    model = NearestNeighbors(n_neighbors=n_neighbors, metric="cosine")
    model.fit(candidates.values)
    distances, indices = model.kneighbors([profile_vector])

    game_names = {game.id: game.name for game in request.catalog}
    # Retourner un RecommendedGame pour chaque paire de distance et index
    return [
        RecommendedGame(
            game_id=candidates.index[idx],
            game_name=game_names.get(candidates.index[idx], ""),
            score=round(1 - float(distance), 4),
        )
        for distance, idx in zip(distances[0], indices[0])
    ]


def _build_user_profile(feature_matrix: pd.DataFrame, request: RecommendationRequest) -> Optional[np.ndarray]:
    vectors = []
    weights = []

    for game_id in request.purchased_game_ids:
        if game_id in feature_matrix.index:
            vectors.append(feature_matrix.loc[game_id].values)
            weights.append(PURCHASE_WEIGHT)

    purchased_ids = set(request.purchased_game_ids)
    for game_id in request.wishlisted_game_ids:
        if game_id in feature_matrix.index and game_id not in purchased_ids:
            vectors.append(feature_matrix.loc[game_id].values)
            weights.append(WISHLIST_WEIGHT)

    if not vectors:
        return None
    # Calcul de la moyenne pour chaque feature d'un jeu
    return np.average(vectors, axis=0, weights=weights)
