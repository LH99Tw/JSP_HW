<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <div class="mb-4">
                <i class="fas fa-exclamation-triangle fa-5x text-warning"></i>
            </div>
            <h1 class="display-1 fw-bold">404</h1>
            <h2 class="mb-4">페이지를 찾을 수 없습니다</h2>
            <p class="text-muted mb-4">
                요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.
            </p>
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

