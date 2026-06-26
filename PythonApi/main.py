# Launch API
#   cd /Users/francois/Desktop/GUP/GamesUp/PythonApi
#   python3 -m venv venv
#   source venv/bin/activate
#   pip install -r requirements.txt
#   uvicorn main:app --reload --port 8000

from fastapi import FastAPI, HTTPException

from models import RecommendationRequest, RecommendationResponse
from recommendation import generate_recommendations

app = FastAPI(title="GamesUp Recommendation API")


@app.get("/")
async def root():
    return {"message": "API de recommandation de jeux"}


@app.post("/recommendations", response_model=RecommendationResponse)
async def get_recommendations(request: RecommendationRequest):
    try:
        recommendations = generate_recommendations(request)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    return RecommendationResponse(user_id=request.user_id, recommendations=recommendations)
