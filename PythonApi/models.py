from typing import List, Optional

from pydantic import BaseModel, Field


class GameFeatures(BaseModel):
    id: str
    name: str
    genre_names: List[str] = Field(default_factory=list)
    category_names: List[str] = Field(default_factory=list)
    author_names: List[str] = Field(default_factory=list)
    publisher_name: Optional[str] = None


class RecommendationRequest(BaseModel):
    user_id: str
    purchased_game_ids: List[str] = Field(default_factory=list)
    wishlisted_game_ids: List[str] = Field(default_factory=list)
    catalog: List[GameFeatures]
    top_n: int = 5


class RecommendedGame(BaseModel):
    game_id: str
    game_name: str
    score: float


class RecommendationResponse(BaseModel):
    user_id: str
    recommendations: List[RecommendedGame]
