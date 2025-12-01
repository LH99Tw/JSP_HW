<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow">
                <div class="card-body p-5">
                    <h2 class="card-title text-center mb-4">로그인</h2>
                    
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
                    
                    <form method="post" action="${pageContext.request.contextPath}/login">
                        <input type="hidden" name="redirect" value="${param.redirect}">
                        
                        <div class="mb-3">
                            <label for="username" class="form-label">사용자명</label>
                            <input type="text" class="form-control" id="username" name="username" 
                                   value="${not empty param.username ? param.username : cookie.rememberedUsername.value}" 
                                   required autofocus>
                        </div>
                        
                        <div class="mb-3">
                            <label for="password" class="form-label">비밀번호</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                            <label class="form-check-label" for="rememberMe">
                                아이디 저장
                            </label>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">로그인</button>
                        </div>
                    </form>
                    
                    <div class="text-center mt-3">
                        <p>계정이 없으신가요? 
                            <a href="${pageContext.request.contextPath}/register">회원가입</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

