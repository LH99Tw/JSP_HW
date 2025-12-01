-- 데이터베이스 초기화 스크립트

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'USER', -- USER / ADMIN
    language VARCHAR(10) DEFAULT 'ko',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 파일 업로드 테이블
CREATE TABLE IF NOT EXISTS uploads (
    upload_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ML 결과 테이블
CREATE TABLE IF NOT EXISTS ml_results (
    result_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    upload_id INTEGER REFERENCES uploads(upload_id) ON DELETE CASCADE,
    model_type VARCHAR(50),
    input_data TEXT,
    prediction_result TEXT,
    confidence_score DECIMAL(5,4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사케 기본 정보 테이블
CREATE TABLE IF NOT EXISTS sake (
    sake_id SERIAL PRIMARY KEY,
    name_ja VARCHAR(100) NOT NULL,
    name_en VARCHAR(100),
    name_ko VARCHAR(100),
    brand VARCHAR(100),
    brewery VARCHAR(100),
    region_prefecture VARCHAR(50), -- 홋카이도, 아오모리 등
    style VARCHAR(50),             -- 준마이, 긴조, 다이긴조 등
    alcohol_percent DECIMAL(4,2),
    volume_ml INTEGER,
    polishing_ratio INTEGER,       -- 정미율 (%)
    nihonshu_do DECIMAL(4,1),      -- 일본주도(±)
    acidity DECIMAL(4,2),
    sweetness_level INTEGER,       -- UI용 스케일 (1=카라구치, 5=아마구치)
    aroma_type VARCHAR(50),        -- fruity, floral, rice 등
    body_level INTEGER,            -- 1=라이트, 5=풀바디
    thumbnail_path VARCHAR(500),   -- 대표 이미지 경로
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사케 리뷰 테이블
CREATE TABLE IF NOT EXISTS sake_review (
    review_id SERIAL PRIMARY KEY,
    sake_id INTEGER REFERENCES sake(sake_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5), -- 1~5 별점
    title VARCHAR(100),
    content TEXT NOT NULL,
    sweetness_score INTEGER CHECK (sweetness_score >= 1 AND sweetness_score <= 5),
    dryness_score INTEGER CHECK (dryness_score >= 1 AND dryness_score <= 5),
    acidity_score INTEGER CHECK (acidity_score >= 1 AND acidity_score <= 5),
    aroma_score INTEGER CHECK (aroma_score >= 1 AND aroma_score <= 5),
    body_score INTEGER CHECK (body_score >= 1 AND body_score <= 5),
    ml_tags TEXT,                             -- ML 분석 태그 (JSON)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 사케 상품 테이블 (판매용)
CREATE TABLE IF NOT EXISTS sake_product (
    product_id SERIAL PRIMARY KEY,
    sake_id INTEGER REFERENCES sake(sake_id) ON DELETE CASCADE,
    price INTEGER NOT NULL CHECK (price >= 0),
    stock INTEGER DEFAULT 0 CHECK (stock >= 0),
    is_published BOOLEAN DEFAULT TRUE,
    label VARCHAR(50), -- 한정판/프리미엄 등 태그
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 장바구니 테이블
CREATE TABLE IF NOT EXISTS cart_item (
    cart_item_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES sake_product(product_id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id) -- 같은 상품은 하나의 레코드로만 존재
);

-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    total_amount INTEGER NOT NULL CHECK (total_amount >= 0),
    status VARCHAR(20) DEFAULT 'REQUESTED', -- REQUESTED, PROCESSING, COMPLETED, CANCELLED
    receiver_name VARCHAR(100),
    address TEXT,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 주문 상품 테이블
CREATE TABLE IF NOT EXISTS order_item (
    order_item_id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES sake_product(product_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price INTEGER NOT NULL CHECK (price >= 0) -- 주문 시점의 가격 (변동 대비)
);

-- 게시판 게시글 테이블
CREATE TABLE IF NOT EXISTS post (
    post_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    category VARCHAR(50) DEFAULT '일반', -- 공지, 일반, 질문, 리뷰(술), 주점, 주판점, 양조장, 여행후기 등
    prefix VARCHAR(50), -- 말머리 (설문, AD 등)
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    is_notice BOOLEAN DEFAULT FALSE, -- 공지글 여부
    is_pinned BOOLEAN DEFAULT FALSE, -- 상단 고정 여부
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 게시글 댓글 테이블
CREATE TABLE IF NOT EXISTS post_comment (
    comment_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES post(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    parent_comment_id INTEGER REFERENCES post_comment(comment_id) ON DELETE CASCADE, -- 대댓글용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 게시글 추천 테이블
CREATE TABLE IF NOT EXISTS post_like (
    like_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES post(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id) -- 한 사용자는 한 게시글에 한 번만 추천 가능
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_uploads_user_id ON uploads(user_id);
CREATE INDEX IF NOT EXISTS idx_ml_results_user_id ON ml_results(user_id);

-- 사케 도메인 인덱스
CREATE INDEX IF NOT EXISTS idx_sake_region ON sake(region_prefecture);
CREATE INDEX IF NOT EXISTS idx_sake_style ON sake(style);
CREATE INDEX IF NOT EXISTS idx_sake_brand ON sake(brand);
CREATE INDEX IF NOT EXISTS idx_sake_review_sake_id ON sake_review(sake_id);
CREATE INDEX IF NOT EXISTS idx_sake_review_user_id ON sake_review(user_id);
CREATE INDEX IF NOT EXISTS idx_sake_product_sake_id ON sake_product(sake_id);
CREATE INDEX IF NOT EXISTS idx_cart_item_user_id ON cart_item(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_item(order_id);

-- 게시판 인덱스
CREATE INDEX IF NOT EXISTS idx_post_user_id ON post(user_id);
CREATE INDEX IF NOT EXISTS idx_post_category ON post(category);
CREATE INDEX IF NOT EXISTS idx_post_created_at ON post(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_post_like_count ON post(like_count DESC);
CREATE INDEX IF NOT EXISTS idx_post_is_notice ON post(is_notice);
CREATE INDEX IF NOT EXISTS idx_post_is_pinned ON post(is_pinned);
CREATE INDEX IF NOT EXISTS idx_post_comment_post_id ON post_comment(post_id);
CREATE INDEX IF NOT EXISTS idx_post_comment_user_id ON post_comment(user_id);
CREATE INDEX IF NOT EXISTS idx_post_like_post_id ON post_like(post_id);
CREATE INDEX IF NOT EXISTS idx_post_like_user_id ON post_like(user_id);

-- 초기 테스트 데이터 삽입
-- 관리자 계정 (비밀번호: admin123, SHA-256 해시)
INSERT INTO users (username, email, password, full_name, role) 
VALUES ('admin', 'admin@saketime.com', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', '관리자', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- 샘플 사케 데이터
INSERT INTO sake (name_ja, name_en, name_ko, brand, brewery, region_prefecture, style, alcohol_percent, volume_ml, polishing_ratio, nihonshu_do, acidity, sweetness_level, aroma_type, body_level) VALUES
('獺祭', 'Dassai', '닷사이 23', '닷사이', '아사히 주조', '야마구치', '준마이다이긴조', 16.0, 720, 23, 3.0, 1.3, 3, 'fruity', 3),
('久保田', 'Kubota', '쿠보타 만주', '쿠보타', '아사히 주조', '니가타', '준마이긴조', 15.0, 720, 50, 2.0, 1.2, 2, 'floral', 2),
('八海山', 'Hakkaisan', '핫카이산 준마이', '핫카이산', '남에쓰 주조', '니가타', '준마이', 15.5, 720, 60, 1.5, 1.4, 3, 'rice', 4),
('新政', 'Aramasa', '아라마사 히노토리', '아라마사', '아라마사 주조', '아키타', '준마이다이긴조', 16.0, 720, 40, 2.5, 1.3, 4, 'fruity', 3),
('十四代', 'Juyondai', '쥬욘다이', '쥬욘다이', '타카사고 주조', '야마가타', '준마이다이긴조', 15.0, 720, 35, 3.5, 1.2, 2, 'floral', 2)
ON CONFLICT DO NOTHING;

-- 샘플 게시글 데이터
INSERT INTO post (user_id, category, prefix, title, content, view_count, like_count, comment_count, is_notice) VALUES
(1, '공지', NULL, '한국내 주류 개인거래는 불법입니다.', '한국내 주류 개인거래는 불법입니다. 관련 법규를 준수해주세요.', 100, 5, 3, TRUE),
(1, '공지', NULL, '통합공지 최신본 ver 04.10', '통합공지 최신본입니다. 확인 부탁드립니다.', 80, 3, 2, TRUE),
(1, '일반', NULL, '뉴비인데 어떻게 추천받아야 할지 모르겠어요!', '사케 추천 받고 싶은데 어떻게 해야 할까요?', 50, 10, 6, FALSE),
(1, '질문', NULL, '초심자의 일본 현지에서의 니혼슈 구매 관련', '일본 여행 중 사케 구매하려고 하는데 추천 부탁드립니다.', 30, 5, 4, FALSE),
(1, '일반', NULL, '갤럼들이 만드는 니혼슈갤 이자카야 맵 Beta', '이자카야 맵을 만들어봤습니다. 피드백 부탁드립니다!', 120, 15, 10, FALSE)
ON CONFLICT DO NOTHING;

-- 샘플 상품 데이터
INSERT INTO sake_product (sake_id, price, stock, is_published, label) VALUES
(1, 45000, 10, TRUE, NULL),
(2, 38000, 15, TRUE, NULL),
(3, 42000, 8, TRUE, NULL),
(4, 55000, 5, TRUE, '프리미엄'),
(5, 120000, 3, TRUE, '한정판')
ON CONFLICT DO NOTHING;

