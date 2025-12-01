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
    <title>파일 업로드 - JSP Homework</title>
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
                        <span>파일 업로드</span>
                    </div>
                    <div style="padding: 1rem; color: var(--vscode-text-secondary);">
                        <p>파일을 업로드하세요.</p>
                    </div>
                </div>
            </div>
            
            <!-- 중앙 작업 영역 -->
            <div class="vscode-workspace">
                <div class="vscode-tabs" id="tabs-container"></div>
                <div class="vscode-editor" id="editor-container">
                    <div style="padding: 2rem; max-width: 800px; margin: 0 auto;">
                        <h2 style="margin-bottom: 1.5rem; color: var(--vscode-text);">파일 업로드</h2>
                        
                        <%-- 에러 메시지 --%>
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                            <div style="padding: 1rem; margin-bottom: 1rem; background-color: #fee; color: #c33; border-radius: 4px; border: 1px solid #fcc;">
                                <%= error %>
                            </div>
                        <%
                            }
                        %>
                        
                        <%-- 성공 메시지 --%>
                        <%
                            String success = (String) request.getAttribute("success");
                            com.example.model.FileInfo fileInfo = (com.example.model.FileInfo) request.getAttribute("fileInfo");
                            if (success != null || fileInfo != null) {
                        %>
                            <div style="padding: 1rem; margin-bottom: 1rem; background-color: #efe; color: #3c3; border-radius: 4px; border: 1px solid #cfc;">
                                <%
                                    if (success != null) {
                                        out.print(success);
                                    }
                                    if (fileInfo != null) {
                                %>
                                    <div style="margin-top: 1rem;">
                                        <p><strong>파일명:</strong> <%= fileInfo.getOriginalFilename() %></p>
                                        <p><strong>크기:</strong> <%= fileInfo.getFormattedFileSize() %></p>
                                        <p style="margin-top: 0.5rem;">
                                            <a href="${pageContext.request.contextPath}/" style="color: var(--vscode-accent);">파일 탐색기로 이동</a>
                                        </p>
                                    </div>
                                <%
                                    }
                                %>
                            </div>
                        <%
                            }
                        %>
                        
                        <%-- 파일 업로드 폼 --%>
                        <form method="post" action="${pageContext.request.contextPath}/upload" 
                              enctype="multipart/form-data" onsubmit="return validateFile()"
                              style="background-color: var(--vscode-panel-bg); padding: 1.5rem; border-radius: 4px; border: 1px solid var(--vscode-border);">
                            <div style="margin-bottom: 1.5rem;">
                                <label for="file" style="display: block; margin-bottom: 0.5rem; color: var(--vscode-text); font-weight: 600;">
                                    <fmt:message key="select_file" bundle="${messages}" />
                                </label>
                                <input type="file" id="file" name="file" required
                                       style="width: 100%; padding: 0.75rem; background-color: var(--vscode-bg); color: var(--vscode-text); border: 1px solid var(--vscode-border); border-radius: 2px; box-sizing: border-box;">
                                <small style="color: var(--vscode-text-secondary); font-size: 12px; margin-top: 0.25rem; display: block;">
                                    최대 10MB까지 업로드 가능합니다.
                                </small>
                            </div>
                            
                            <div style="display: flex; gap: 1rem;">
                                <button type="submit" 
                                        style="padding: 0.75rem 2rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer; font-size: 14px;">
                                    <fmt:message key="upload" bundle="${messages}" />
                                </button>
                                <a href="${pageContext.request.contextPath}/" 
                                   style="padding: 0.75rem 2rem; background-color: var(--vscode-sidebar-bg); color: var(--vscode-text); text-decoration: none; border-radius: 2px; display: inline-block; border: 1px solid var(--vscode-border);">
                                    취소
                                </a>
                            </div>
                        </form>
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
        function validateFile() {
            var fileInput = document.getElementById("file");
            var file = fileInput.files[0];
            
            if (!file) {
                alert("파일을 선택해주세요.");
                return false;
            }
            
            var maxSize = 10 * 1024 * 1024; // 10MB
            if (file.size > maxSize) {
                alert("파일 크기는 10MB를 초과할 수 없습니다.");
                return false;
            }
            
            return true;
        }
        
        // 하단 패널 토글
        function togglePanel() {
            let panel = document.getElementById('bottom-panel');
            if (panel) {
                panel.classList.toggle('visible');
            }
        }
        
        // Activity 전환
        function switchActivity(activity) {
            // 구현 필요 시 추가
        }
    </script>
    <jsp:include page="/jsp/common/vscode-footer.jspf" />
</body>
</html>
