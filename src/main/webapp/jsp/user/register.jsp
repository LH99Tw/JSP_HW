<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-body p-5">
                    <h2 class="card-title text-center mb-4">회원가입</h2>
                    
                    <!-- 에러 메시지 표시 -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <!-- 성공 메시지 표시 -->
                    <c:if test="${not empty success}">
                        <div class="alert alert-success" role="alert">
                            ${success}
                        </div>
                    </c:if>
                    
                    <form id="registerForm" method="post" action="${pageContext.request.contextPath}/register">
                        <div class="mb-3">
                            <label for="username" class="form-label">사용자명 *</label>
                            <input type="text" class="form-control" id="username" name="username" 
                                   value="${username}" required minlength="3" maxlength="50">
                            <div class="form-text">3자 이상 50자 이하</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="email" class="form-label">이메일 *</label>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="${email}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="password" class="form-label">비밀번호 *</label>
                            <input type="password" class="form-control" id="password" name="password" 
                                   required minlength="6">
                            <div class="form-text">6자 이상</div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="passwordConfirm" class="form-label">비밀번호 확인 *</label>
                            <input type="password" class="form-control" id="passwordConfirm" 
                                   name="passwordConfirm" required minlength="6">
                            <div id="passwordMatch" class="form-text"></div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="fullName" class="form-label">이름 *</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" 
                                   value="${fullName}" required>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">회원가입</button>
                        </div>
                    </form>
                    
                    <div class="text-center mt-3">
                        <p>이미 계정이 있으신가요? 
                            <a href="${pageContext.request.contextPath}/login">로그인</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// 클라이언트 측 유효성 검사
document.getElementById('registerForm').addEventListener('submit', function(e) {
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('passwordConfirm').value;
    
    if (password !== passwordConfirm) {
        e.preventDefault();
        document.getElementById('passwordMatch').textContent = '비밀번호가 일치하지 않습니다.';
        document.getElementById('passwordMatch').style.color = 'red';
        return false;
    } else {
        document.getElementById('passwordMatch').textContent = '비밀번호가 일치합니다.';
        document.getElementById('passwordMatch').style.color = 'green';
    }
});

// 비밀번호 확인 실시간 검증
document.getElementById('passwordConfirm').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const passwordConfirm = this.value;
    const matchDiv = document.getElementById('passwordMatch');
    
    if (passwordConfirm.length > 0) {
        if (password === passwordConfirm) {
            matchDiv.textContent = '비밀번호가 일치합니다.';
            matchDiv.style.color = 'green';
        } else {
            matchDiv.textContent = '비밀번호가 일치하지 않습니다.';
            matchDiv.style.color = 'red';
        }
    } else {
        matchDiv.textContent = '';
    }
});
</script>

<%@ include file="/jsp/common/footer.jspf" %>

