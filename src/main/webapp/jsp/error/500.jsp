<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - 서버 오류</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-container {
            text-align: center;
            margin: 100px auto;
            max-width: 600px;
        }
        .error-code {
            font-size: 72px;
            color: #e74c3c;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-code">500</div>
        <h1>서버 오류가 발생했습니다</h1>
        <p>서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>
        <a href="${pageContext.request.contextPath}/" class="btn">홈으로 돌아가기</a>
    </div>
</body>
</html>

