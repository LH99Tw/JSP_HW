<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JSP Homework - Main</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>JSP Homework Application</h1>
    <p>환영합니다!</p>
    
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/jsp/user/login.jsp">로그인</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/user/register.jsp">회원가입</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/user/upload.jsp">파일 업로드</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/user/ml-analyze.jsp">ML 분석</a></li>
        </ul>
    </nav>
    
    <%
        // 세션 확인 예제
        String username = (String) session.getAttribute("username");
        if (username != null) {
            out.println("<p>현재 로그인 사용자: " + username + "</p>");
        } else {
            out.println("<p>로그인하지 않았습니다.</p>");
        }
    %>
</body>
</html>

