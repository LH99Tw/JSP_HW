#!/bin/bash

# 프로젝트 시작 스크립트

set -e

echo "🚀 JSP Homework 프로젝트 시작..."

# 초기 설정이 안 되어 있으면 실행
if [ ! -d "src/main/webapp/WEB-INF/lib" ] || [ -z "$(ls -A src/main/webapp/WEB-INF/lib 2>/dev/null)" ]; then
    echo "📦 초기 설정이 필요합니다. 설정을 실행합니다..."
    bash scripts/setup.sh
    echo ""
fi

# Docker Compose로 시작
echo "🐳 Docker Compose로 서비스 시작..."
cd docker
docker-compose up -d

echo ""
echo "✅ 서비스가 시작되었습니다!"
echo ""
echo "접속 정보:"
echo "  - JSP 애플리케이션: http://localhost:8080"
echo "  - ML 서비스: http://localhost:8000"
echo "  - ML 서비스 헬스 체크: http://localhost:8000/health"
echo ""
echo "서비스 상태 확인:"
echo "  docker-compose ps"
echo ""
echo "로그 확인:"
echo "  docker-compose logs -f"
echo ""
echo "서비스 중지:"
echo "  docker-compose down"

