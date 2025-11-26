# JSP Homework Project

JSP/Servlet 기반 웹 애플리케이션과 FastAPI ML 서비스를 통합한 프로젝트입니다.

## 프로젝트 구조

```
JSP_HW/
├── src/main/
│   ├── java/              # Java 서블릿 및 유틸리티
│   ├── webapp/            # JSP 페이지 및 웹 리소스
│   └── resources/         # 리소스 파일 (i18n 등)
├── ml-service/            # FastAPI ML 서비스
├── docker/                # Docker 설정 파일
└── .github/workflows/     # CI/CD 설정
```

## 기술 스택

- **Backend**: JSP/Servlet, Java 11+
- **ML Service**: FastAPI, Python 3.9+
- **Database**: PostgreSQL 14+
- **WAS**: Apache Tomcat 9.x
- **Container**: Docker, Docker Compose

## 사전 요구사항

- JDK 11 이상
- Python 3.9 이상
- Docker & Docker Compose
- Git

## 로컬 개발 환경 설정

### 1. 데이터베이스 설정

PostgreSQL을 Docker로 실행:

```bash
docker run -d \
  --name postgres \
  -e POSTGRES_DB=jsp_hw \
  -e POSTGRES_USER=jsp_user \
  -e POSTGRES_PASSWORD=jsp_password \
  -p 5432:5432 \
  postgres:14
```

### 2. 데이터베이스 초기화

```bash
psql -h localhost -U jsp_user -d jsp_hw -f docker/init.sql
```

### 3. ML 서비스 실행

```bash
cd ml-service
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### 4. Tomcat 실행

#### 방법 1: Docker 사용

```bash
cd docker
docker-compose up -d
```

#### 방법 2: 로컬 Tomcat 사용

1. Apache Tomcat 9.x 다운로드 및 설치
2. `src/main/webapp` 디렉토리를 `$CATALINA_HOME/webapps/ROOT`에 복사
3. 필요한 JAR 파일을 `WEB-INF/lib`에 추가:
   - JSTL 1.2
   - PostgreSQL JDBC Driver
   - 기타 필요한 라이브러리
4. Tomcat 시작

## 필요한 라이브러리

`WEB-INF/lib` 디렉토리에 다음 JAR 파일들을 추가해야 합니다:

- **JSTL**: `jstl-1.2.jar`, `standard.jar`
- **PostgreSQL JDBC**: `postgresql-42.x.x.jar`
- **JSON 처리** (선택): `jackson-core.jar`, `jackson-databind.jar`
- **로깅** (선택): `log4j-api.jar`, `log4j-core.jar`

## 빠른 시작 (Quick Start)

### 방법 1: 자동 스크립트 사용 (권장)

```bash
# 초기 설정 + 시작 (한 번에)
bash scripts/start.sh
```

또는 단계별로:

```bash
# 1. 초기 설정 (라이브러리 다운로드)
bash scripts/setup.sh

# 2. Docker Compose로 시작
cd docker
docker-compose up -d
```

### 방법 2: 수동 설정

```bash
# 1. 필요한 라이브러리 다운로드
bash scripts/download-libs.sh

# 2. Docker Compose로 시작
cd docker
docker-compose up -d
```

## 서비스 접속 정보

- **JSP 애플리케이션**: http://localhost:8080
- **ML 서비스**: http://localhost:8000
- **ML 서비스 헬스 체크**: http://localhost:8000/health
- **PostgreSQL**: localhost:5432

## 유용한 명령어

```bash
# 서비스 상태 확인
cd docker && docker-compose ps

# 로그 확인
cd docker && docker-compose logs -f

# 특정 서비스 로그만
cd docker && docker-compose logs -f tomcat
cd docker && docker-compose logs -f ml-service

# 서비스 중지
cd docker && docker-compose down

# 서비스 중지 및 볼륨 삭제
cd docker && docker-compose down -v
```

## CI/CD

GitHub Actions를 통해 다음을 자동으로 체크합니다:

- Java 코드 문법 검사
- JSP 파일 기본 문법 검사
- Python 코드 문법 검사
- requirements.txt 의존성 충돌 검사
- Docker 설정 파일 검증

## 주요 기능

- ✅ 사용자 인증 및 권한 관리 (세션/쿠키)
- ✅ 파일 업로드 및 다운로드
- ✅ 다국어 지원 (i18n)
- ✅ 머신러닝 모델 연동
- ✅ 데이터베이스 CRUD 작업

## 개발 가이드

자세한 개발 명세는 [개발명세.md](./개발명세.md)를 참고하세요.

## 라이선스

이 프로젝트는 교육 목적으로 제작되었습니다.

