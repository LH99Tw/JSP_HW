# SAKETIME - 사케 커뮤니티 & 마켓

JSP/Servlet 기반 웹 애플리케이션과 FastAPI ML 서비스를 통합한 일본 사케 전문 커뮤니티 및 마켓 플랫폼입니다.

## 🎯 프로젝트 개요

SAKETIME은 일본 사케에 대한 정보 공유, 리뷰 작성, 커뮤니티 활동, 그리고 사케 구매까지 제공하는 통합 플랫폼입니다.

### 주요 기능

- **커뮤니티**: 게시판, 댓글, 추천 기능
- **사케 정보**: 사케 목록, 상세 정보, 리뷰
- **마켓**: 상품 목록, 장바구니, 주문
- **관리자**: 사케/상품/리뷰/주문 관리
- **ML 추천**: 사용자 맞춤 사케 추천
- **다국어 지원**: 한국어, 영어, 일본어

## 🛠 기술 스택

- **Backend**: JSP/Servlet, Java 11+
- **ML Service**: FastAPI, Python 3.9+
- **Database**: PostgreSQL 14+
- **WAS**: Apache Tomcat 9.x
- **Container**: Docker, Docker Compose
- **Frontend**: Bootstrap 5, JSTL, EL

## 📋 사전 요구사항

- JDK 11 이상
- Python 3.9 이상 (ML 서비스용)
- Docker & Docker Compose
- Git

## 🚀 빠른 시작

### 1. 프로젝트 클론

```bash
git clone <repository-url>
cd JSP_HW
```

### 2. Docker Compose로 전체 서비스 실행

```bash
cd docker
docker-compose up -d
```

이 명령어는 다음 서비스들을 자동으로 시작합니다:
- PostgreSQL (포트 5432)
- ML Service (포트 8000)
- Tomcat (포트 8080)

### 3. 접속

- **웹 애플리케이션**: http://localhost:8080
- **ML 서비스 API**: http://localhost:8000
- **ML 서비스 문서**: http://localhost:8000/docs

### 4. 초기 관리자 계정

- **사용자명**: admin
- **비밀번호**: admin123 (초기 설정 후 변경 권장)

## 📁 프로젝트 구조

```
JSP_HW/
├── src/main/
│   ├── java/              # Java 서블릿 및 유틸리티
│   │   └── com/example/
│   │       ├── dao/       # 데이터 접근 객체
│   │       ├── model/     # 모델 클래스
│   │       ├── servlet/   # 서블릿
│   │       ├── filter/    # 필터
│   │       └── util/       # 유틸리티
│   ├── webapp/            # JSP 페이지 및 웹 리소스
│   │   ├── jsp/           # JSP 페이지
│   │   ├── css/           # 스타일시트
│   │   └── WEB-INF/       # 설정 파일
│   └── resources/         # 리소스 파일
├── ml-service/            # FastAPI ML 서비스
│   └── app/
│       └── main.py        # ML 서비스 메인
├── docker/                # Docker 설정 파일
│   ├── docker-compose.yml
│   ├── Dockerfile.tomcat
│   └── init.sql          # DB 초기화 스크립트
└── README.md
```

## 🔧 개발 환경 설정

### 로컬 개발 (Docker 없이)

#### 1. 데이터베이스 설정

```bash
# PostgreSQL 실행
docker run -d \
  --name postgres \
  -e POSTGRES_DB=jsp_hw \
  -e POSTGRES_USER=jsp_user \
  -e POSTGRES_PASSWORD=jsp_password \
  -p 5432:5432 \
  postgres:14

# DB 초기화
psql -h localhost -U jsp_user -d jsp_hw -f docker/init.sql
```

#### 2. ML 서비스 실행

```bash
cd ml-service
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

#### 3. Tomcat 설정

1. Apache Tomcat 9.x 다운로드 및 설치
2. `src/main/webapp` 디렉토리를 `$CATALINA_HOME/webapps/ROOT`에 복사
3. 필요한 JAR 파일을 `WEB-INF/lib`에 추가:
   - JSTL 1.2 (`jstl-1.2.jar`, `standard.jar`)
   - PostgreSQL JDBC Driver (`postgresql-42.7.1.jar`)
4. 환경 변수 설정:
   ```bash
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=jsp_hw
   export DB_USER=jsp_user
   export DB_PASSWORD=jsp_password
   export ML_SERVICE_URL=http://localhost:8000
   ```
5. Tomcat 시작

## 📚 주요 기능 설명

### 1. 커뮤니티
- 게시판 목록 (전체글/인기글/카테고리별)
- 게시글 작성/수정/삭제
- 댓글 작성/삭제
- 추천 기능

### 2. 사케 정보
- 사케 목록 (지역/스타일 필터)
- 사케 상세 정보
- 리뷰 작성/수정/삭제
- ML 기반 맛 태그 자동 분석

### 3. 마켓
- 상품 목록 (검색/필터)
- 상품 상세
- 장바구니 (추가/수정/삭제)
- 주문 (폼/생성/완료)

### 4. 관리자
- 대시보드 (통계)
- 사케 관리 (CRUD)
- 상품 관리 (CRUD)
- 리뷰 관리
- 주문 관리

### 5. ML 추천
- 사용자 맞춤 사케 추천
- 리뷰 텍스트 분석 (맛 태그 추출)

## 🌐 다국어 지원

- 한국어 (기본)
- 영어
- 일본어

언어는 헤더의 드롭다운에서 변경할 수 있으며, 쿠키에 저장됩니다.

## 🔐 보안

- SQL Injection 방지: PreparedStatement 사용
- XSS 방지: JSTL/EL 자동 이스케이프
- 세션 관리: HttpSession 활용
- 비밀번호 해싱: BCrypt (PasswordUtil)
- 관리자 권한 필터: AdminAuthFilter

## 🧪 테스트

### 주요 엔드포인트 테스트

```bash
# 게시판
curl http://localhost:8080/board/list

# 상품 목록
curl http://localhost:8080/product/list

# 추천
curl http://localhost:8080/recommend

# ML 서비스 헬스체크
curl http://localhost:8000/health

# ML 추천 API
curl -X POST http://localhost:8000/api/v1/recommend \
  -H "Content-Type: application/json" \
  -d '{"user_id": 1}'

# ML 맛 분석 API
curl -X POST http://localhost:8000/api/v1/analyze-taste \
  -H "Content-Type: application/json" \
  -d '{"review_text": "달콤하고 과일향이 풍부한 사케입니다"}'
```

## 🐳 Docker 명령어

```bash
# 서비스 시작
cd docker
docker-compose up -d

# 서비스 중지
docker-compose down

# 로그 확인
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs -f tomcat
docker-compose logs -f ml-service

# 서비스 재빌드
docker-compose build --no-cache
docker-compose up -d

# DB 접속
docker exec -it jsp-hw-postgres psql -U jsp_user -d jsp_hw
```

## 📝 환경 변수

### Tomcat 컨테이너
- `DB_HOST`: 데이터베이스 호스트 (기본값: postgres)
- `DB_PORT`: 데이터베이스 포트 (기본값: 5432)
- `DB_NAME`: 데이터베이스 이름 (기본값: jsp_hw)
- `DB_USER`: 데이터베이스 사용자 (기본값: jsp_user)
- `DB_PASSWORD`: 데이터베이스 비밀번호 (기본값: jsp_password)
- `ML_SERVICE_URL`: ML 서비스 URL (기본값: http://ml-service:8000)

## 🐛 문제 해결

### 포트 충돌
포트가 이미 사용 중인 경우:
```bash
# 포트 확인
lsof -i :8080
lsof -i :8000
lsof -i :5432

# docker-compose.yml에서 포트 변경
```

### DB 연결 오류
```bash
# DB 컨테이너 상태 확인
docker-compose ps postgres

# DB 로그 확인
docker-compose logs postgres
```

### Tomcat 시작 실패
```bash
# Tomcat 로그 확인
docker-compose logs tomcat

# 컨테이너 재빌드
docker-compose build --no-cache tomcat
docker-compose up -d tomcat
```

## 📄 라이선스

이 프로젝트는 교육용으로 제작되었습니다.

## 👥 기여

프로젝트 개선 제안이나 버그 리포트는 이슈로 등록해주세요.

---

**개발 완료일**: 2025년 12월
**버전**: 1.0.0
