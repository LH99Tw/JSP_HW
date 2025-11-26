#!/bin/bash

# 필요한 라이브러리 다운로드 스크립트

LIB_DIR="src/main/webapp/WEB-INF/lib"
mkdir -p "$LIB_DIR"

echo "📦 필요한 라이브러리 다운로드 중..."

# JSTL 1.2
echo "다운로드: JSTL 1.2"
curl -L -o "$LIB_DIR/jstl-1.2.jar" \
  https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar

curl -L -o "$LIB_DIR/standard.jar" \
  https://repo1.maven.org/maven2/taglibs/standard/1.1.2/standard-1.1.2.jar

# PostgreSQL JDBC Driver
echo "다운로드: PostgreSQL JDBC Driver"
curl -L -o "$LIB_DIR/postgresql-42.7.1.jar" \
  https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.1/postgresql-42.7.1.jar

echo "✅ 라이브러리 다운로드 완료!"
echo "다운로드된 파일:"
ls -lh "$LIB_DIR"

