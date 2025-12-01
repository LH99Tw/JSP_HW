<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - JSP Homework</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 1rem;
        }
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
        }
        .form-group input[type="text"],
        .form-group input[type="password"] {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .form-group input[type="checkbox"] {
            margin-right: 0.5rem;
        }
        .btn {
            width: 100%;
            padding: 0.75rem;
            background-color: #2c3e50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
        }
        .btn:hover {
            background-color: #34495e;
        }
        .error {
            color: red;
            margin-bottom: 1rem;
            padding: 0.75rem;
            background-color: #fee;
            border-radius: 4px;
        }
        .register-link {
            text-align: center;
            margin-top: 1rem;
        }
        .register-link a {
            color: #2c3e50;
            text-decoration: none;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>로그인</h1>
        
        <%-- 에러 메시지 표시 --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
        %>
        
        <%-- 로그인 폼 --%>
        <form method="post" action="${pageContext.request.contextPath}/login" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username">사용자명</label>
                <input type="text" id="username" name="username" 
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label>
                    <input type="checkbox" name="rememberMe"> 자동 로그인
                </label>
            </div>
            
            <button type="submit" class="btn">로그인</button>
        </form>
        
        <div class="register-link">
            <p>계정이 없으신가요? <a href="${pageContext.request.contextPath}/register">회원가입</a></p>
            <p style="margin-top: 0.5rem;"><a href="${pageContext.request.contextPath}/">← 홈으로 돌아가기</a></p>
        </div>
    </div>
    
    <script>
        function validateForm() {
            var username = document.getElementById("username").value.trim();
            var password = document.getElementById("password").value;
            
            if (username === "") {
                alert("사용자명을 입력해주세요.");
                return false;
            }
            
            if (password === "") {
                alert("비밀번호를 입력해주세요.");
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>

