<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - JSP Homework</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .register-container {
            max-width: 500px;
            margin: 50px auto;
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
        .form-group input[type="email"],
        .form-group input[type="password"] {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
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
        .login-link {
            text-align: center;
            margin-top: 1rem;
        }
        .login-link a {
            color: #2c3e50;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <h1>회원가입</h1>
        
        <%-- 에러 메시지 표시 --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
        %>
        
        <%-- 회원가입 폼 --%>
        <form method="post" action="${pageContext.request.contextPath}/register" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username">사용자명 *</label>
                <input type="text" id="username" name="username" 
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" 
                       required minlength="3" maxlength="20">
                <small>3자 이상 20자 이하</small>
            </div>
            
            <div class="form-group">
                <label for="email">이메일 *</label>
                <input type="email" id="email" name="email" 
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="password">비밀번호 *</label>
                <input type="password" id="password" name="password" required minlength="6">
                <small>6자 이상</small>
            </div>
            
            <div class="form-group">
                <label for="passwordConfirm">비밀번호 확인 *</label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" required>
            </div>
            
            <div class="form-group">
                <label for="fullName">이름 *</label>
                <input type="text" id="fullName" name="fullName" 
                       value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>" 
                       required>
            </div>
            
            <button type="submit" class="btn">회원가입</button>
        </form>
        
        <div class="login-link">
            <p>이미 계정이 있으신가요? <a href="${pageContext.request.contextPath}/login">로그인</a></p>
            <p style="margin-top: 0.5rem;"><a href="${pageContext.request.contextPath}/">← 홈으로 돌아가기</a></p>
        </div>
    </div>
    
    <script>
        function validateForm() {
            var username = document.getElementById("username").value.trim();
            var email = document.getElementById("email").value.trim();
            var password = document.getElementById("password").value;
            var passwordConfirm = document.getElementById("passwordConfirm").value;
            var fullName = document.getElementById("fullName").value.trim();
            
            if (username === "" || username.length < 3 || username.length > 20) {
                alert("사용자명은 3자 이상 20자 이하여야 합니다.");
                return false;
            }
            
            if (email === "") {
                alert("이메일을 입력해주세요.");
                return false;
            }
            
            var emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
            if (!emailRegex.test(email)) {
                alert("올바른 이메일 형식이 아닙니다.");
                return false;
            }
            
            if (password === "" || password.length < 6) {
                alert("비밀번호는 6자 이상이어야 합니다.");
                return false;
            }
            
            if (password !== passwordConfirm) {
                alert("비밀번호가 일치하지 않습니다.");
                return false;
            }
            
            if (fullName === "") {
                alert("이름을 입력해주세요.");
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>

