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
    <title>파일 목록 - JSP Homework</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .list-container {
            max-width: 1000px;
            margin: 50px auto;
            padding: 2rem;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #2c3e50;
            color: white;
            font-weight: bold;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .btn {
            padding: 0.5rem 1rem;
            background-color: #2c3e50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-right: 0.5rem;
        }
        .btn:hover {
            background-color: #34495e;
        }
        .btn-danger {
            background-color: #e74c3c;
        }
        .btn-danger:hover {
            background-color: #c0392b;
        }
        .empty {
            text-align: center;
            padding: 3rem;
            color: #999;
        }
    </style>
</head>
<body>
    <div class="list-container">
        <div class="header">
            <h1><fmt:message key="file_list" bundle="${messages}" /></h1>
            <div style="display: flex; gap: 1rem; align-items: center;">
                <%-- 언어 선택 --%>
                <form method="post" action="${pageContext.request.contextPath}/language" style="display: inline;">
                    <select name="lang" onchange="this.form.submit()" style="padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px;">
                        <option value="ko" ${language == 'ko' ? 'selected' : ''}>한국어</option>
                        <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
                        <option value="ja" ${language == 'ja' ? 'selected' : ''}>日本語</option>
                    </select>
                </form>
                <%-- 검색 폼 --%>
                <form method="get" action="${pageContext.request.contextPath}/search" style="display: flex; gap: 0.5rem;">
                    <input type="text" name="q" 
                           placeholder="<fmt:message key="search_placeholder" bundle="${messages}" />" 
                           value="${param.q != null ? param.q : ''}"
                           style="padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px;">
                    <button type="submit" class="btn" style="width: auto;"><fmt:message key="search" bundle="${messages}" /></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/files" class="btn" style="width: auto;"><fmt:message key="total_files" bundle="${messages}" /></a>
                    </c:if>
                </form>
                <a href="${pageContext.request.contextPath}/upload" class="btn"><fmt:message key="file_upload" bundle="${messages}" /></a>
                <a href="${pageContext.request.contextPath}/" class="btn"><fmt:message key="home" bundle="${messages}" /></a>
            </div>
        </div>
        
        <%-- 검색 결과 카운트 --%>
        <c:if test="${not empty param.q}">
            <div style="margin-bottom: 1rem; color: #666;">
                <fmt:message key="search_results" bundle="${messages}" />: "<strong>${param.q}</strong>" - ${resultCount != null ? resultCount : 0} <fmt:message key="file" bundle="${messages}" />
            </div>
        </c:if>
        
        <%-- 성공/에러 메시지 --%>
        <%
            String success = request.getParameter("success");
            if ("deleted".equals(success)) {
        %>
            <div style="color: green; margin-bottom: 1rem; padding: 0.75rem; background-color: #efe; border-radius: 4px;">
                파일이 삭제되었습니다.
            </div>
        <%
            } else if ("saved".equals(success)) {
        %>
            <div style="color: green; margin-bottom: 1rem; padding: 0.75rem; background-color: #efe; border-radius: 4px;">
                파일이 저장되었습니다.
            </div>
        <%
            }
            
            String error = request.getParameter("error");
            if ("delete_failed".equals(error)) {
        %>
            <div style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border-radius: 4px;">
                파일 삭제에 실패했습니다.
            </div>
        <%
            }
        %>
        
        <%-- 파일 목록 테이블 --%>
        <c:choose>
            <c:when test="${empty files}">
                <div class="empty">
                    <p><fmt:message key="no_files" bundle="${messages}" /></p>
                    <a href="${pageContext.request.contextPath}/upload" class="btn"><fmt:message key="upload_file" bundle="${messages}" /></a>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th><fmt:message key="file_name" bundle="${messages}" /></th>
                            <th><fmt:message key="file_size" bundle="${messages}" /></th>
                            <th><fmt:message key="file_type" bundle="${messages}" /></th>
                            <th><fmt:message key="upload_date" bundle="${messages}" /></th>
                            <th><fmt:message key="actions" bundle="${messages}" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="file" items="${files}">
                            <%
                                // LocalDateTime을 Date로 변환 (변수명 충돌 방지)
                                com.example.model.FileInfo fileItem = (com.example.model.FileInfo) pageContext.getAttribute("file");
                                java.util.Date uploadDate = null;
                                if (fileItem != null && fileItem.getUploadedAt() != null) {
                                    uploadDate = com.example.util.DateUtil.toDate(fileItem.getUploadedAt());
                                }
                                pageContext.setAttribute("uploadDate", uploadDate);
                            %>
                            <tr>
                                <td>${file.originalFilename}</td>
                                <td>${file.formattedFileSize}</td>
                                <td>${file.contentType}</td>
                                <td>
                                    <c:if test="${uploadDate != null}">
                                        <fmt:formatDate value="${uploadDate}" pattern="yyyy-MM-dd HH:mm" />
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${file.contentType != null && (file.contentType.startsWith('text/') || file.originalFilename.endsWith('.txt') || file.originalFilename.endsWith('.java') || file.originalFilename.endsWith('.jsp') || file.originalFilename.endsWith('.js') || file.originalFilename.endsWith('.css') || file.originalFilename.endsWith('.html') || file.originalFilename.endsWith('.md'))}">
                                            <a href="${pageContext.request.contextPath}/edit?id=${file.uploadId}" class="btn"><fmt:message key="file_edit" bundle="${messages}" /></a>
                                        </c:when>
                                    </c:choose>
                                    <a href="${pageContext.request.contextPath}/download?id=${file.uploadId}" class="btn"><fmt:message key="file_download" bundle="${messages}" /></a>
                                    <form method="post" action="${pageContext.request.contextPath}/delete" 
                                          style="display: inline;" onsubmit="return confirm('<fmt:message key="delete_confirm" bundle="${messages}" />');">
                                        <input type="hidden" name="id" value="${file.uploadId}">
                                        <button type="submit" class="btn btn-danger"><fmt:message key="file_delete" bundle="${messages}" /></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>

