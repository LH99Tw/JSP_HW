<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>오류 발생</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-container {
            text-align: center;
            margin: 100px auto;
            max-width: 600px;
        }
        .error-message {
            color: #e74c3c;
            margin: 2rem 0;
            padding: 1rem;
            background-color: #fee;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>오류가 발생했습니다</h1>
        <div class="error-message">
            <%
                if (exception != null) {
                    out.println("<p><strong>오류 메시지:</strong> " + exception.getMessage() + "</p>");
                } else {
                    out.println("<p>알 수 없는 오류가 발생했습니다.</p>");
                }
            %>
        </div>
        <a href="${pageContext.request.contextPath}/" class="btn">홈으로 돌아가기</a>
    </div>
</body>
</html>

