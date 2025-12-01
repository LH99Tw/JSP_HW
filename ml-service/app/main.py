"""
FastAPI ML Service
"""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional
import uvicorn

app = FastAPI(title="ML Service", version="1.0.0")

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class HealthResponse(BaseModel):
    status: str
    message: str


@app.get("/")
async def root():
    """루트 엔드포인트 - API 정보 및 문서 링크"""
    return {
        "service": "ML Service",
        "version": "1.0.0",
        "status": "running",
        "endpoints": {
            "health": "/health",
            "models": "/api/v1/models",
            "predict": "/api/v1/predict",
            "classify": "/api/v1/classify",
            "docs": "/docs",
            "redoc": "/redoc"
        }
    }


@app.get("/health", response_model=HealthResponse)
async def health_check():
    """헬스 체크 엔드포인트"""
    return HealthResponse(status="ok", message="ML Service is running")


@app.get("/api/v1/models")
async def get_models():
    """사용 가능한 모델 목록"""
    return {
        "models": [
            {"id": "image-classifier", "name": "Image Classifier", "type": "classification"},
            {"id": "text-analyzer", "name": "Text Analyzer", "type": "analysis"}
        ]
    }


class PredictRequest(BaseModel):
    model_type: str
    input_data: str
    user_id: Optional[int] = None


class PredictResponse(BaseModel):
    prediction: str
    confidence: float
    model_type: str


@app.post("/api/v1/predict", response_model=PredictResponse)
async def predict(request: PredictRequest):
    """예측 요청"""
    # TODO: 실제 ML 모델 연동
    return PredictResponse(
        prediction="sample_prediction",
        confidence=0.95,
        model_type=request.model_type
    )


@app.post("/api/v1/classify")
async def classify(request: PredictRequest):
    """분류 요청"""
    # TODO: 실제 ML 모델 연동
    return {
        "class": "sample_class",
        "confidence": 0.95,
        "model_type": request.model_type
    }


# 사케 추천 요청 모델
class RecommendRequest(BaseModel):
    user_id: int
    preferences: Optional[dict] = None


class RecommendResponse(BaseModel):
    sake_ids: list[int]
    reason: Optional[str] = None


@app.post("/api/v1/recommend", response_model=RecommendResponse)
async def recommend(request: RecommendRequest):
    """사케 추천 API (더미 버전)"""
    # TODO: 실제 ML 모델 연동
    # 현재는 더미 데이터 반환
    # 실제로는 사용자의 리뷰 히스토리, 선호도 등을 기반으로 추천
    
    # 더미 추천: 인기 사케 ID 1-5 반환
    dummy_recommendations = [1, 2, 3, 4, 5]
    
    return RecommendResponse(
        sake_ids=dummy_recommendations,
        reason="Based on your preferences and popular items"
    )


# 맛 분석 요청 모델
class AnalyzeTasteRequest(BaseModel):
    review_text: str


class AnalyzeTasteResponse(BaseModel):
    tags: list[str]
    confidence: Optional[float] = None


@app.post("/api/v1/analyze-taste", response_model=AnalyzeTasteResponse)
async def analyze_taste(request: AnalyzeTasteRequest):
    """리뷰 텍스트 분석 - 맛 태그 추출 (더미 버전)"""
    # TODO: 실제 ML 모델 연동 (NLP, 키워드 추출 등)
    # 현재는 키워드 기반 더미 로직
    
    review_text = request.review_text.lower()
    tags = []
    
    # 키워드 기반 태그 추출 (더미)
    taste_keywords = {
        "sweet": ["달콤", "sweet", "甘い", "아마구치"],
        "dry": ["드라이", "dry", "辛口", "카라구치"],
        "fruity": ["과일", "fruit", "フルーティ", "프루티"],
        "floral": ["꽃", "flower", "フローラル", "플로럴"],
        "rich": ["풍부", "rich", "豊か", "리치"],
        "light": ["가벼운", "light", "軽い", "라이트"]
    }
    
    for tag, keywords in taste_keywords.items():
        if any(keyword in review_text for keyword in keywords):
            tags.append(tag)
    
    # 태그가 없으면 기본 태그 반환
    if not tags:
        tags = ["balanced"]
    
    return AnalyzeTasteResponse(
        tags=tags,
        confidence=0.8
    )


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

