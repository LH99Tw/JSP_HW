# UI 설계 문서

## 1. 전체 레이아웃 구조

### 1.1 VSCode 스타일 레이아웃
```
┌─────────────────────────────────────────────────────────────┐
│                    상단 메뉴 바 (Menu Bar)                    │
├──┬──────────────────────────────────────────────────────────┤
│  │                                                           │
│A │  ┌──────────────────────────────────────────────────┐   │
│c │  │                                                  │   │
│t │  │         중앙 에디터 영역 (Editor Area)            │   │
│i │  │                                                  │   │
│v │  │  ┌──────────────────────────────────────────┐   │   │
│i │  │  │  탭 바 (Tabs)                             │   │   │
│t │  │  └──────────────────────────────────────────┘   │   │
│y │  │                                                  │   │
│  │  │  ┌──────────────────────────────────────────┐   │   │
│B │  │  │  파일 내용 뷰어/에디터                     │   │   │
│a │  │  │                                          │   │   │
│r │  │  │                                          │   │   │
│  │  │  └──────────────────────────────────────────┘   │   │
│  │  │                                                  │   │
│  │  └──────────────────────────────────────────────────┘   │
│  │                                                           │
│  │  ┌──────────────────────────────────────────────────┐   │
│  │  │  우측 사이드바 (Right Sidebar) - 메모 패널        │   │
│  │  └──────────────────────────────────────────────────┘   │
├──┴──────────────────────────────────────────────────────────┤
│                    하단 상태 바 (Status Bar)                  │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 구성 요소

#### A. Activity Bar (좌측 최외곽)
- **위치**: 화면 최좌측, 세로로 배치된 아이콘 바
- **너비**: 48px (고정)
- **기능**: 
  - 📁 **탐색기 (Explorer)**: 파일 목록 표시
  - 📊 **시각화 (Visualization)**: 데이터 시각화 (Phase 3)
  - ⚙️ **설정 (Settings)**: 언어 설정, 테마 등
  - 🔍 **검색 (Search)**: 파일 검색 (향후 확장)
- **상태**: 선택된 Activity는 배경색이 다르게 표시

#### B. Sidebar (Activity Bar 옆)
- **위치**: Activity Bar 바로 옆
- **너비**: 250px (기본), 조절 가능
- **기능**: 선택한 Activity에 따라 내용 변경
  - **탐색기 선택 시**: 파일 트리 표시
  - **시각화 선택 시**: 시각화 옵션 및 차트
  - **설정 선택 시**: 언어, 테마 등 설정 옵션

#### C. Editor Area (중앙)
- **위치**: Sidebar와 Right Sidebar 사이
- **구성**:
  - **탭 바**: 열린 파일들을 탭으로 표시
  - **에디터/뷰어**: 선택한 파일의 내용 표시
    - 텍스트 파일: 편집 가능한 에디터 또는 읽기 전용 뷰어
    - PDF: iframe으로 표시
    - 이미지: 이미지 뷰어
    - 기타: 다운로드 링크 제공

#### D. Right Sidebar (우측)
- **위치**: 화면 최우측
- **너비**: 300px (기본), 토글 가능
- **기능**: 메모 패널
  - 현재 열린 파일과 연동된 메모 작성
  - 파일별 메모 저장 (Phase 3)

#### E. Menu Bar (상단)
- **위치**: 화면 최상단
- **높이**: 35px
- **구성**:
  - 좌측: 검색창 (명령어 팔레트 기능 포함)
  - 우측: 사용자 정보, 로그아웃, 파일 업로드 링크

#### F. Status Bar (하단)
- **위치**: 화면 최하단
- **높이**: 22px
- **구성**:
  - 좌측: 현재 사용자, 파일 개수 등
  - 우측: 언어 설정, 테마 등

## 2. 상세 UI 컴포넌트

### 2.1 Activity Bar

```html
<div class="activity-bar">
  <button class="activity-item active" data-activity="explorer" title="탐색기">
    <span class="icon">📁</span>
  </button>
  <button class="activity-item" data-activity="visualization" title="시각화">
    <span class="icon">📊</span>
  </button>
  <button class="activity-item" data-activity="settings" title="설정">
    <span class="icon">⚙️</span>
  </button>
  <button class="activity-item" data-activity="search" title="검색">
    <span class="icon">🔍</span>
  </button>
</div>
```

**스타일**:
- 배경색: `#2d2d30`
- 활성 아이템 배경색: `#37373d`
- 아이콘 크기: 24px
- 호버 효과: 배경색 변경

### 2.2 Sidebar (탐색기 Activity 선택 시)

```html
<div class="sidebar">
  <div class="sidebar-header">
    <span>탐색기</span>
    <button class="sidebar-action" title="새 파일">+</button>
    <button class="sidebar-action" title="새 폴더">📁</button>
    <button class="sidebar-action" title="새로고침">🔄</button>
  </div>
  <div class="sidebar-content">
    <ul class="file-tree">
      <li class="file-item" data-file-id="1">
        <span class="icon">📄</span>
        <span class="name">example.txt</span>
      </li>
      <!-- ... -->
    </ul>
  </div>
</div>
```

**기능**:
- 파일 클릭 시 중앙 에디터에 탭으로 열기
- 파일 우클릭: 컨텍스트 메뉴 (편집, 다운로드, 삭제)
- 폴더 확장/축소 (향후 구현)

### 2.3 Sidebar (시각화 Activity 선택 시)

```html
<div class="sidebar">
  <div class="sidebar-header">시각화</div>
  <div class="sidebar-content">
    <div class="visualization-options">
      <h3>차트 타입</h3>
      <select>
        <option>파일 타입별 분포</option>
        <option>업로드 날짜별 통계</option>
        <option>파일 크기 분포</option>
      </select>
      <!-- 차트 영역 -->
      <div class="chart-container"></div>
    </div>
  </div>
</div>
```

### 2.4 Sidebar (설정 Activity 선택 시)

```html
<div class="sidebar">
  <div class="sidebar-header">설정</div>
  <div class="sidebar-content">
    <div class="setting-section">
      <h3>언어</h3>
      <select name="language">
        <option value="ko">한국어</option>
        <option value="en">English</option>
        <option value="ja">日本語</option>
      </select>
    </div>
    <div class="setting-section">
      <h3>테마</h3>
      <select name="theme">
        <option value="dark">다크</option>
        <option value="light">라이트</option>
      </select>
    </div>
  </div>
</div>
```

### 2.5 Editor Area

```html
<div class="editor-area">
  <div class="tabs-container">
    <div class="tab active" data-file-id="1">
      <span class="tab-name">example.txt</span>
      <button class="tab-close">×</button>
    </div>
    <!-- ... -->
  </div>
  <div class="editor-content">
    <!-- 파일 내용 -->
  </div>
</div>
```

**기능**:
- 탭 클릭: 해당 파일로 전환
- 탭 닫기: 파일 닫기
- 파일 내용 표시:
  - 텍스트: 편집 가능한 textarea 또는 읽기 전용 pre
  - PDF: iframe
  - 이미지: img 태그
  - 기타: 다운로드 링크

### 2.6 Right Sidebar (메모 패널)

```html
<div class="right-sidebar" id="memo-panel">
  <div class="sidebar-header">
    <span>메모</span>
    <button class="sidebar-close" onclick="toggleMemo()">×</button>
  </div>
  <div class="memo-content">
    <textarea id="memo-text" placeholder="파일과 연동된 메모를 작성하세요..."></textarea>
    <button onclick="saveMemo()">저장</button>
  </div>
</div>
```

## 3. 색상 테마

### 3.1 다크 테마 (기본)
```css
--vscode-bg: #1e1e1e;              /* 메인 배경 */
--vscode-sidebar-bg: #252526;      /* 사이드바 배경 */
--vscode-panel-bg: #181818;        /* 패널 배경 */
--vscode-text: #cccccc;            /* 기본 텍스트 */
--vscode-text-secondary: #858585;  /* 보조 텍스트 */
--vscode-border: #3e3e42;          /* 테두리 */
--vscode-hover: #2a2d2e;           /* 호버 배경 */
--vscode-active: #094771;          /* 활성 배경 */
--vscode-accent: #007acc;           /* 강조 색상 */
--activity-bar-bg: #2d2d30;        /* Activity Bar 배경 */
--activity-bar-active: #37373d;    /* Activity Bar 활성 */
```

### 3.2 라이트 테마 (향후 구현)
```css
--vscode-bg: #ffffff;
--vscode-sidebar-bg: #f3f3f3;
--vscode-panel-bg: #f8f8f8;
--vscode-text: #333333;
--vscode-text-secondary: #666666;
--vscode-border: #e0e0e0;
--vscode-hover: #e8e8e8;
--vscode-active: #cce5ff;
--vscode-accent: #0078d4;
```

## 4. 반응형 디자인

### 4.1 화면 크기별 레이아웃
- **데스크톱 (≥1024px)**: 전체 레이아웃 표시
- **태블릿 (768px~1023px)**: 
  - Right Sidebar 숨김 (토글 버튼으로 표시)
  - Sidebar 너비 200px
- **모바일 (<768px)**:
  - Activity Bar와 Sidebar 숨김 (햄버거 메뉴로 토글)
  - Editor Area만 표시

## 5. 주요 기능 및 상호작용

### 5.1 파일 열기
1. Sidebar에서 파일 클릭
2. Editor Area에 새 탭 생성
3. 파일 내용 로드 및 표시
4. 이미 열려있는 파일이면 해당 탭으로 전환

### 5.2 파일 편집
1. 텍스트 파일의 경우 "편집" 버튼 클릭
2. 편집 모드로 전환 (textarea)
3. 저장 버튼 클릭 시 서버에 저장

### 5.3 Activity 전환
1. Activity Bar에서 아이콘 클릭
2. Sidebar 내용이 선택한 Activity에 맞게 변경
3. 활성 Activity 아이콘 배경색 변경

### 5.4 메모 패널
1. 우측 상단 "메모" 버튼 클릭
2. Right Sidebar 토글
3. 메모 작성 후 저장

## 6. 구현 우선순위

### Phase 1 (현재)
- ✅ 기본 레이아웃 구조
- ✅ Activity Bar 구현
- ✅ Sidebar (탐색기) 구현
- ✅ Editor Area (탭 + 뷰어) 구현
- ✅ 파일 열기/닫기 기능

### Phase 2 (다음)
- [ ] Sidebar (시각화) 구현
- [ ] Sidebar (설정) 구현
- [ ] Right Sidebar (메모 패널) 구현
- [ ] 파일 편집 기능 개선
- [ ] 컨텍스트 메뉴 (우클릭)

### Phase 3 (향후)
- [ ] 파일 검색 기능
- [ ] 폴더 구조 지원
- [ ] 테마 전환 기능
- [ ] 메모 서버 연동
- [ ] 드래그 앤 드롭 파일 업로드

## 7. 기술 스택

- **HTML/CSS**: 레이아웃 및 스타일링
- **JavaScript**: 동적 UI 상호작용
- **JSP/JSTL**: 서버 사이드 렌더링
- **AJAX**: 파일 내용 비동기 로드
- **Servlet**: 파일 처리 API

## 8. 접근성 고려사항

- 키보드 네비게이션 지원
- ARIA 레이블 추가
- 색상 대비 비율 준수 (WCAG 2.1 AA)
- 스크린 리더 지원

