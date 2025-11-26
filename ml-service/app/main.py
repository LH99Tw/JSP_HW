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


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

