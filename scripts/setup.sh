#!/bin/bash

# 프로젝트 초기 설정 스크립트

set -e

echo "🚀 JSP Homework 프로젝트 초기 설정 시작..."

# 1. 필요한 라이브러리 다운로드
echo ""
echo "1️⃣ 필요한 라이브러리 다운로드..."
bash scripts/download-libs.sh

# 2. Java 클래스 컴파일 (선택사항)
echo ""
echo "2️⃣ Java 클래스 컴파일..."
if [ -d "src/main/java" ] && [ "$(find src/main/java -name '*.java' | wc -l)" -gt 0 ]; then
    echo "Java 파일 발견, 컴파일 시도..."
    mkdir -p src/main/webapp/WEB-INF/classes
    
    # servlet-api.jar 다운로드 (컴파일용)
    mkdir -p /tmp/lib
    curl -L -o /tmp/lib/servlet-api.jar \
      https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar 2>/dev/null || true
    
    # 컴파일 시도
    CLASSPATH="/tmp/lib/servlet-api.jar:src/main/webapp/WEB-INF/lib/*"
    find src/main/java -name "*.java" -exec javac -cp "$CLASSPATH" -d src/main/webapp/WEB-INF/classes {} + 2>/dev/null && \
      echo "✅ Java 클래스 컴파일 완료" || \
      echo "⚠️ Java 컴파일 실패 (Tomcat에서 자동 컴파일됨)"
else
    echo "⚠️ Java 파일이 없습니다. (선택사항)"
fi

echo ""
echo "✅ 초기 설정 완료!"
echo ""
echo "다음 단계:"
echo "  cd docker"
echo "  docker-compose up -d"
echo ""
echo "또는 전체를 한 번에:"
echo "  bash scripts/start.sh"

