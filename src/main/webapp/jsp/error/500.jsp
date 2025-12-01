<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isErrorPage="true" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <div class="mb-4">
                <i class="fas fa-server fa-5x text-danger"></i>
            </div>
            <h1 class="display-1 fw-bold">500</h1>
            <h2 class="mb-4">서버 오류</h2>
            <p class="text-muted mb-4">
                서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.
            </p>
            <c:if test="${not empty exception}">
                <div class="alert alert-danger text-start">
                    <strong>오류 메시지:</strong><br>
                    <code>${exception.message}</code>
                </div>
            </c:if>
            <div class="d-flex justify-content-center gap-2">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home"></i> 홈으로
                </a>
                <a href="javascript:history.back()" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> 이전 페이지
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

