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
%>
<fmt:setLocale value="${locale}" />
<fmt:setBundle basename="i18n.messages" var="messages" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>파일 탐색기 - JSP Homework</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vscode-style.css">
</head>
<body>
    <div class="vscode-container">
        <!-- 상단 바 -->
        <div class="vscode-topbar">
            <form method="get" action="${pageContext.request.contextPath}/search" style="flex: 1; max-width: 400px;">
                <input type="text" name="q" 
                       class="search-box" 
                       placeholder="<fmt:message key="search_placeholder" bundle="${messages}" /> (예: setting:--)"
                       value="${param.q != null ? param.q : ''}">
            </form>
            <div class="menu-items">
                <a href="${pageContext.request.contextPath}/upload"><fmt:message key="file_upload" bundle="${messages}" /></a>
                <a href="${pageContext.request.contextPath}/files"><fmt:message key="file_list" bundle="${messages}" /></a>
                <a href="${pageContext.request.contextPath}/"><fmt:message key="home" bundle="${messages}" /></a>
            </div>
        </div>
        
        <!-- 메인 영역 -->
        <div class="vscode-main">
            <!-- 좌측 사이드바 -->
            <div class="vscode-sidebar">
                <div class="sidebar-header">탐색기</div>
                <div class="sidebar-tabs">
                    <button class="sidebar-tab active" onclick="switchTab('explorer')">📁 탐색기</button>
                    <button class="sidebar-tab" onclick="switchTab('visualization')">📊 시각화</button>
                    <button class="sidebar-tab" onclick="switchTab('settings')">⚙️ 설정</button>
                </div>
                <div class="sidebar-content" id="explorer-tab">
                    <ul class="file-tree">
                        <li class="file-tree-item" onclick="loadFiles()">
                            <span class="icon">📁</span>
                            <span class="name">모든 파일</span>
                        </li>
                        <c:forEach var="file" items="${files}">
                            <%
                                // 파일 아이콘 결정
                                com.example.model.FileInfo file = (com.example.model.FileInfo) pageContext.getAttribute("file");
                                String icon = "📄";
                                if (file != null) {
                                    String filename = file.getOriginalFilename().toLowerCase();
                                    if (filename.endsWith(".txt")) icon = "📝";
                                    else if (filename.endsWith(".md")) icon = "📖";
                                    else if (filename.endsWith(".json")) icon = "📋";
                                    else if (filename.endsWith(".pdf")) icon = "📕";
                                    else if (filename.endsWith(".java") || filename.endsWith(".jsp") || filename.endsWith(".js") || filename.endsWith(".css")) icon = "💻";
                                    else if (filename.endsWith(".html") || filename.endsWith(".xml")) icon = "🌐";
                                }
                                pageContext.setAttribute("fileIcon", icon);
                            %>
                            <li class="file-tree-item" onclick="openFile(${file.uploadId}, '${file.originalFilename}')">
                                <span class="icon">${fileIcon}</span>
                                <span class="name">${file.originalFilename}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="sidebar-content" id="visualization-tab" style="display: none;">
                    <p style="padding: 1rem; color: var(--vscode-text-secondary);">
                        <fmt:message key="visualization" bundle="${messages}" /> 기능은 Phase 3에서 구현됩니다.
                    </p>
                </div>
                <div class="sidebar-content" id="settings-tab" style="display: none;">
                    <div style="padding: 1rem;">
                        <h3 style="font-size: 13px; margin-bottom: 0.5rem;"><fmt:message key="language" bundle="${messages}" /></h3>
                        <form method="post" action="${pageContext.request.contextPath}/language">
                            <select name="lang" onchange="this.form.submit()" 
                                    style="width: 100%; padding: 0.5rem; background-color: var(--vscode-bg); color: var(--vscode-text); border: 1px solid var(--vscode-border); border-radius: 2px;">
                                <option value="ko" ${language == 'ko' ? 'selected' : ''}>한국어</option>
                                <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
                                <option value="ja" ${language == 'ja' ? 'selected' : ''}>日本語</option>
                            </select>
                        </form>
                    </div>
                </div>
            </div>
            
            <!-- 중앙 작업 영역 -->
            <div class="vscode-workspace">
                <!-- 탭 바 -->
                <div class="vscode-tabs" id="tabs-container">
                    <!-- 탭은 JavaScript로 동적으로 추가됨 -->
                </div>
                
                <!-- 에디터/뷰어 영역 -->
                <div class="vscode-editor" id="editor-container">
                    <c:choose>
                        <c:when test="${empty files}">
                            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; color: var(--vscode-text-secondary);">
                                <p style="font-size: 18px; margin-bottom: 1rem;">📁</p>
                                <p><fmt:message key="no_files" bundle="${messages}" /></p>
                                <a href="${pageContext.request.contextPath}/upload" 
                                   style="margin-top: 1rem; padding: 0.5rem 1rem; background-color: var(--vscode-accent); color: white; text-decoration: none; border-radius: 2px;">
                                    <fmt:message key="upload_file" bundle="${messages}" />
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="padding: 2rem;">
                                <h2 style="margin-bottom: 1rem;">파일을 선택하세요</h2>
                                <p style="color: var(--vscode-text-secondary);">좌측 사이드바에서 파일을 클릭하여 열 수 있습니다.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <!-- 우측 메모 패널 -->
            <div class="vscode-memo-panel" id="memo-panel">
                <div class="memo-header">
                    <h3>메모</h3>
                    <button class="memo-close" onclick="toggleMemo()">×</button>
                </div>
                <div class="memo-content">
                    <textarea id="memo-text" placeholder="파일과 연동된 메모를 작성하세요..."></textarea>
                    <button onclick="saveMemo()" 
                            style="margin-top: 0.5rem; padding: 0.5rem 1rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer;">
                        저장
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 메모 패널 토글 버튼 (우측 상단) -->
    <button onclick="toggleMemo()" 
            style="position: fixed; top: 40px; right: 20px; z-index: 1000; padding: 0.5rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer; box-shadow: 0 2px 5px rgba(0,0,0,0.3);">
        📝 메모
    </button>
    
    <script>
        let openTabs = [];
        let currentTabId = null;
        
        // 탭 전환
        function switchTab(tabName) {
            document.querySelectorAll('.sidebar-content').forEach(tab => {
                tab.style.display = 'none';
            });
            document.getElementById(tabName + '-tab').style.display = 'block';
            
            document.querySelectorAll('.sidebar-tab').forEach(tab => {
                tab.classList.remove('active');
            });
            event.target.classList.add('active');
        }
        
        // 파일 열기
        function openFile(fileId, filename) {
            // 이미 열려있는 탭인지 확인
            let existingTab = openTabs.find(tab => tab.id === fileId);
            if (existingTab) {
                switchToTab(fileId);
                return;
            }
            
            // 새 탭 추가
            let tab = {
                id: fileId,
                name: filename,
                url: '${pageContext.request.contextPath}/edit?id=' + fileId
            };
            openTabs.push(tab);
            
            // 탭 UI 추가
            addTabToUI(tab);
            
            // 파일 내용 로드
            loadFileContent(fileId, filename);
        }
        
        // 탭 UI에 추가
        function addTabToUI(tab) {
            let tabsContainer = document.getElementById('tabs-container');
            let tabElement = document.createElement('div');
            tabElement.className = 'vscode-tab';
            tabElement.id = 'tab-' + tab.id;
            tabElement.innerHTML = `
                <span>${tab.name}</span>
                <span class="close" onclick="closeTab(${tab.id}, event)">×</span>
            `;
            tabElement.onclick = () => switchToTab(tab.id);
            tabsContainer.appendChild(tabElement);
            
            // 첫 탭이면 활성화
            if (openTabs.length === 1) {
                switchToTab(tab.id);
            }
        }
        
        // 탭 전환
        function switchToTab(fileId) {
            currentTabId = fileId;
            document.querySelectorAll('.vscode-tab').forEach(tab => {
                tab.classList.remove('active');
            });
            let tabElement = document.getElementById('tab-' + fileId);
            if (tabElement) {
                tabElement.classList.add('active');
            }
            
            // 파일 내용 다시 로드
            let tab = openTabs.find(t => t.id === fileId);
            if (tab) {
                loadFileContent(fileId, tab.name);
            }
        }
        
        // 탭 닫기
        function closeTab(fileId, event) {
            event.stopPropagation();
            openTabs = openTabs.filter(tab => tab.id !== fileId);
            let tabElement = document.getElementById('tab-' + fileId);
            if (tabElement) {
                tabElement.remove();
            }
            
            // 닫은 탭이 현재 탭이면 다른 탭으로 전환
            if (currentTabId === fileId) {
                if (openTabs.length > 0) {
                    switchToTab(openTabs[0].id);
                } else {
                    document.getElementById('editor-container').innerHTML = `
                        <div style="padding: 2rem;">
                            <h2 style="margin-bottom: 1rem;">파일을 선택하세요</h2>
                            <p style="color: var(--vscode-text-secondary);">좌측 사이드바에서 파일을 클릭하여 열 수 있습니다.</p>
                        </div>
                    `;
                    currentTabId = null;
                }
            }
        }
        
        // 파일 내용 로드
        function loadFileContent(fileId, filename) {
            let fileExt = filename.split('.').pop().toLowerCase();
            
            // PDF 파일인 경우
            if (fileExt === 'pdf') {
                document.getElementById('editor-container').innerHTML = `
                    <iframe src="${pageContext.request.contextPath}/view?id=${fileId}" 
                            style="width: 100%; height: 100%; border: none; background: white;"></iframe>
                `;
                return;
            }
            
            // 텍스트 파일인 경우
            if (['txt', 'md', 'json', 'java', 'jsp', 'js', 'css', 'html', 'xml', 'properties', 'yml', 'yaml'].includes(fileExt)) {
                fetch('${pageContext.request.contextPath}/view?id=' + fileId)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('HTTP error! status: ' + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.success) {
                            let escapedFilename = escapeHtml(data.filename);
                            let escapedContent = escapeHtml(data.content);
                            
                            // 편집 가능한 텍스트 파일인 경우 편집 버튼 표시
                            if (['txt', 'md', 'json', 'java', 'jsp', 'js', 'css', 'html', 'xml', 'properties'].includes(fileExt)) {
                                document.getElementById('editor-container').innerHTML = `
                                    <div style="display: flex; flex-direction: column; height: 100%;">
                                        <div style="padding: 0.5rem; border-bottom: 1px solid var(--vscode-border); display: flex; justify-content: space-between; align-items: center; background-color: var(--vscode-panel-bg);">
                                            <span style="font-size: 13px; font-weight: 600; color: var(--vscode-text);">${escapedFilename}</span>
                                            <button onclick="editFile(${fileId}, '${escapedFilename.replace(/'/g, "\\'")}')" 
                                                    style="padding: 0.25rem 0.5rem; background-color: var(--vscode-accent); color: white; border: none; border-radius: 2px; cursor: pointer; font-size: 12px;">
                                                편집
                                            </button>
                                        </div>
                                        <pre style="flex: 1; margin: 0; padding: 1rem; overflow: auto; font-family: 'Courier New', monospace; font-size: 14px; line-height: 1.5; background-color: var(--vscode-bg); color: var(--vscode-text); white-space: pre-wrap; word-wrap: break-word;">${escapedContent}</pre>
                                    </div>
                                `;
                            } else {
                                // 읽기 전용 뷰어
                                document.getElementById('editor-container').innerHTML = `
                                    <div style="display: flex; flex-direction: column; height: 100%;">
                                        <div style="padding: 0.5rem; border-bottom: 1px solid var(--vscode-border); background-color: var(--vscode-panel-bg);">
                                            <span style="font-size: 13px; font-weight: 600; color: var(--vscode-text);">${escapedFilename}</span>
                                        </div>
                                        <pre style="flex: 1; margin: 0; padding: 1rem; overflow: auto; font-family: 'Courier New', monospace; font-size: 14px; line-height: 1.5; background-color: var(--vscode-bg); color: var(--vscode-text); white-space: pre-wrap; word-wrap: break-word;">${escapedContent}</pre>
                                    </div>
                                `;
                            }
                        } else {
                            document.getElementById('editor-container').innerHTML = `
                                <div style="padding: 2rem; text-align: center;">
                                    <p style="color: var(--vscode-text-secondary);">${data.message || '파일을 불러올 수 없습니다.'}</p>
                                </div>
                            `;
                        }
                    })
                    .catch(error => {
                        console.error('Error loading file:', error);
                        document.getElementById('editor-container').innerHTML = `
                            <div style="padding: 2rem; text-align: center;">
                                <h2 style="color: var(--vscode-text);">${escapeHtml(filename)}</h2>
                                <p style="color: var(--vscode-text-secondary);">파일을 불러오는 중 오류가 발생했습니다.</p>
                            </div>
                        `;
                    });
            } else if (fileExt === 'pdf') {
                // PDF는 이미 처리됨
                return;
            } else {
                // 지원하지 않는 파일 타입
                document.getElementById('editor-container').innerHTML = `
                    <div style="padding: 2rem; text-align: center;">
                        <p style="color: var(--vscode-text-secondary);">이 파일 타입은 미리보기를 지원하지 않습니다.</p>
                        <a href="${pageContext.request.contextPath}/download?id=${fileId}" 
                           style="margin-top: 1rem; display: inline-block; padding: 0.5rem 1rem; background-color: var(--vscode-accent); color: white; text-decoration: none; border-radius: 2px;">
                            다운로드
                        </a>
                    </div>
                `;
            }
        }
        
        // HTML 이스케이프
        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }
        
        // 파일 편집 모드로 전환
        function editFile(fileId, filename) {
            window.location.href = '${pageContext.request.contextPath}/edit?id=' + fileId;
        }
        
        // 모든 파일 로드
        function loadFiles() {
            window.location.href = '${pageContext.request.contextPath}/files';
        }
        
        // 메모 패널 토글
        function toggleMemo() {
            let panel = document.getElementById('memo-panel');
            panel.classList.toggle('visible');
        }
        
        // 메모 저장
        function saveMemo() {
            let memoText = document.getElementById('memo-text').value;
            // TODO: 서버에 메모 저장
            alert('메모가 저장되었습니다. (Phase 3에서 구현 예정)');
        }
        
        // 설정 명령어 처리
        document.querySelector('.search-box').addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                let query = this.value.trim();
                if (query.startsWith('setting:')) {
                    e.preventDefault();
                    switchTab('settings');
                    this.value = '';
                }
            }
        });
    </script>
</body>
</html>

