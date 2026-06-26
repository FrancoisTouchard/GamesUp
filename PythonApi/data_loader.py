from typing import List

import pandas as pd
from sklearn.preprocessing import MultiLabelBinarizer

from models import GameFeatures


def build_game_feature_matrix(catalog: List[GameFeatures]) -> pd.DataFrame:
    # Multi-hot encoder pour créer une matrice de valeurs binaires pour les genres/categories/authors/publisher de chaque jeu
    blocks = [
        _encode_multi_valued([game.genre_names for game in catalog], "genre"),
        _encode_multi_valued([game.category_names for game in catalog], "category"),
        _encode_multi_valued([game.author_names for game in catalog], "author"),
        _encode_multi_valued(
            [[game.publisher_name] if game.publisher_name else [] for game in catalog], "publisher"
        ),
    ]
    # Matrice = jeux en lignes et genres/categories/authors/publisher en colonnes
    matrix = pd.concat(blocks, axis=1)
    matrix.index = [game.id for game in catalog]
    return matrix

# Conversion des valeurs en binaire pour lecture par le modèle
def _encode_multi_valued(values: List[List[str]], prefix: str) -> pd.DataFrame:
    binarizer = MultiLabelBinarizer()
    encoded = binarizer.fit_transform(values)
    columns = [f"{prefix}__{label}" for label in binarizer.classes_]
    return pd.DataFrame(encoded, columns=columns)
