<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    // 로그인 체크
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    com.example.model.FileInfo fileInfo = (com.example.model.FileInfo) request.getAttribute("fileInfo");
    if (fileInfo == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
    
    // 언어 설정
    String language = (String) session.getAttribute("language");
    if (language == null) {
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if ("language".equals(cookie.getName())) {
                    language = cookie.getValue();
                    session.setAttribute("language", language);
                    break;
                }
            }
        }
        if (language == null) {
            language = "ko";
        }
    }
    
    java.util.Locale locale;
    switch (language) {
        case "en":
            locale = java.util.Locale.ENGLISH;
            break;
        case "ja":
            locale = java.util.Locale.JAPANESE;
            break;
        default:
            locale = java.util.Locale.KOREAN;
    }
    pageContext.setAttribute("locale", locale);
    pageContext.setAttribute("language", language);
%>
<fmt:setLocale value="${locale}" />
<fmt:setBundle basename="i18n.messages" var="messages" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>파일 편집 - ${fileInfo.originalFilename}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vscode-style.css">
</head>
<body>
    <div class="vscode-container">
        <!-- 상단 메뉴 바 -->
        <div class="vscode-topbar">
            <input type="text" 
                   id="search-input"
                   class="search-box" 
                   placeholder="<fmt:message key="search_placeholder" bundle="${messages}" /> (예: setting:--)"
                   onclick="openSearchOverlay()">
            <div class="menu-items">
                <span>${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/logout"><fmt:message key="logout" bundle="${messages}" /></a>
            </div>
        </div>
        
        <!-- 검색 오버레이 -->
        <div class="search-overlay" id="search-overlay" onclick="closeSearchOverlayOnBackdrop(event)">
            <div class="search-overlay-content" onclick="event.stopPropagation()">
                <div class="search-overlay-header">
                    <input type="text" 
                           id="search-overlay-input"
                           class="search-input" 
                           placeholder="파일 검색 (예: edit.jsp 또는 :10 줄로 이동 또는 @function 기호로 이동)"
                           autofocus>
                </div>
                <div class="search-overlay-tabs">
                    <div class="search-overlay-tab active" data-tab="files" onclick="switchSearchTab('files')">파일로 이동</div>
                    <div class="search-overlay-tab" data-tab="commands" onclick="switchSearchTab('commands')">명령 표시 및 실행</div>
                    <div class="search-overlay-tab" data-tab="text" onclick="switchSearchTab('text')">텍스트 검색</div>
                </div>
                <div class="search-overlay-results" id="search-results">
                    <div class="search-overlay-empty">검색어를 입력하세요...</div>
                </div>
            </div>
        </div>
        
        <!-- 메인 영역 -->
        <div class="vscode-main">
            <!-- Activity Bar (좌측 최외곽) -->
            <div class="activity-bar">
                <button class="activity-item" data-activity="explorer" title="탐색기" onclick="window.location.href='${pageContext.request.contextPath}/'">
                    <span class="icon">📁</span>
                </button>
                <button class="activity-item" data-activity="visualization" title="시각화" onclick="switchActivity('visualization')">
                    <span class="icon">📊</span>
                </button>
                <button class="activity-item" data-activity="settings" title="설정" onclick="switchActivity('settings')">
                    <span class="icon">⚙️</span>
                </button>
            </div>
            
            <!-- 좌측 사이드바 -->
            <div class="vscode-sidebar">
                <div class="sidebar-content" id="explorer-content">
                    <div class="sidebar-header">
                        <span>파일 편집</span>
                    </div>
                    <div style="padding: 1rem; color: var(--vscode-text-secondary);">
                        <p>${fileInfo.originalFilename}</p>
                    </div>
                </div>
            </div>
            
            <!-- 중앙 작업 영역 -->
            <div class="vscode-workspace">
                <div class="vscode-tabs" id="tabs-container">
                    <div class="vscode-tab active">
                        <span>${fileInfo.originalFilename}</span>
                    </div>
                </div>
                <div class="vscode-editor" id="editor-container">
                    <div style="display: flex; flex-direction: column; height: 100%;">
                        <div style="padding: 0.5rem 1rem; border-bottom: 1px solid var(--vscode-border); display: flex; justify-content: space-between; align-items: center; background-color: var(--vscode-panel-bg);">
                            <span style="font-size: 13px; font-weight: 600; color: var(--vscode-text);">
                                <fmt:message key="file_edit" bundle="${messages}" />: ${fileInfo.originalFilename}
                            </span>
                            <div style="display: flex; gap: 0.5rem;">
                                <button onclick="saveFile()" 
                                        style="padding: 0.25rem 0.75rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer; font-size: 12px;">
                                    저장
                                </button>
                                <button onclick="cancelEdit()" 
                                        style="padding: 0.25rem 0.75rem; background-color: var(--vscode-sidebar-bg); color: var(--vscode-text); border: 1px solid var(--vscode-border); border-radius: 2px; cursor: pointer; font-size: 12px;">
                                    취소
                                </button>
                            </div>
                        </div>
                        
                        <form id="edit-form" method="post" action="${pageContext.request.contextPath}/edit" style="flex: 1; display: flex; flex-direction: column; overflow: hidden;">
                            <input type="hidden" name="id" value="${fileInfo.uploadId}">
                            <textarea id="file-content" name="content" 
                                      style="flex: 1; margin: 0; padding: 1rem; overflow: auto; font-family: 'Courier New', monospace; font-size: 14px; line-height: 1.5; background-color: var(--vscode-bg); color: var(--vscode-text); border: none; resize: none; white-space: pre; tab-size: 4;">${fileContent}</textarea>
                        </form>
                        
                        <%-- 에러 메시지 --%>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                            <div id="error-message" style="padding: 0.75rem 1rem; background-color: #fee; color: #c33; border-top: 1px solid #fcc; font-size: 13px;">
                                <%= error %>
                            </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 하단 패널 (메모) -->
        <div class="vscode-panel" id="bottom-panel">
            <div class="vscode-panel-header">
                <div class="vscode-panel-tabs">
                    <div class="vscode-panel-tab active">메모</div>
                </div>
                <button class="vscode-panel-close" onclick="togglePanel()" title="패널 닫기">×</button>
            </div>
            <div class="vscode-panel-content">
                <div class="vscode-memo-panel">
                    <textarea id="memo-text" placeholder="파일과 연동된 메모를 작성하세요..." style="width: 100%; min-height: 200px; background-color: var(--vscode-bg); color: var(--vscode-text); border: 1px solid var(--vscode-border); border-radius: 2px; padding: 0.5rem; font-family: 'Courier New', monospace; font-size: 13px; resize: vertical;"></textarea>
                    <button onclick="saveMemo()" 
                            style="margin-top: 0.5rem; padding: 0.5rem 1rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer;">
                        저장
                    </button>
                </div>
            </div>
        </div>
        
        <!-- 하단 상태 바 -->
        <div class="vscode-status-bar">
            <div class="status-bar-left">
                <span>${sessionScope.username}</span>
            </div>
            <div class="status-bar-right">
                <button onclick="togglePanel()" style="background: none; border: none; color: var(--vscode-text-secondary); cursor: pointer; padding: 0.25rem 0.5rem; font-size: 12px;" title="메모 패널 토글">
                    📝 메모
                </button>
                <span><fmt:message key="language" bundle="${messages}" />: ${language == 'ko' ? '한국어' : language == 'en' ? 'English' : '日本語'}</span>
            </div>
        </div>
    </div>
    
    <script>
        function saveFile() {
            document.getElementById('edit-form').submit();
        }
        
        function cancelEdit() {
            if (confirm('편집 내용을 저장하지 않고 나가시겠습니까?')) {
                window.location.href = '${pageContext.request.contextPath}/';
            }
        }
        
        // Ctrl+S로 저장
        document.addEventListener('keydown', function(e) {
            if ((e.ctrlKey || e.metaKey) && e.key === 's') {
                e.preventDefault();
                saveFile();
            }
        });
        
        // 변경 감지
        let originalContent = document.getElementById('file-content').value;
        let isDirty = false;
        
        document.getElementById('file-content').addEventListener('input', function() {
            isDirty = this.value !== originalContent;
        });
        
        window.addEventListener('beforeunload', function(e) {
            if (isDirty) {
                e.preventDefault();
                e.returnValue = '';
            }
        });
        
        // 하단 패널 토글
        function togglePanel() {
            let panel = document.getElementById('bottom-panel');
            if (panel) {
                panel.classList.toggle('visible');
            }
        }
        
        // 메모 저장
        function saveMemo() {
            let memoText = document.getElementById('memo-text').value;
            // TODO: 서버에 메모 저장
            alert('메모가 저장되었습니다. (Phase 3에서 구현 예정)');
        }
        
        // Activity 전환
        function switchActivity(activity) {
            // 구현 필요 시 추가
        }
    </script>
    <jsp:include page="/jsp/common/vscode-footer.jspf" />
</body>
</html>
