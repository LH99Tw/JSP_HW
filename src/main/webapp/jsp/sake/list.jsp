<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 브레드크럼 -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">홈</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sake/list">커뮤니티</a></li>
            <li class="breadcrumb-item active">사케 목록</li>
        </ol>
    </nav>
    
    <h1 class="mb-4">사케 목록</h1>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/sake/list" class="row g-3">
                <div class="col-md-3">
                    <label for="region" class="form-label">원산지</label>
                    <select class="form-select" id="region" name="region">
                        <option value="">전체</option>
                        <option value="홋카이도" ${region == '홋카이도' ? 'selected' : ''}>홋카이도</option>
                        <option value="니가타" ${region == '니가타' ? 'selected' : ''}>니가타</option>
                        <option value="야마가타" ${region == '야마가타' ? 'selected' : ''}>야마가타</option>
                        <option value="후쿠시마" ${region == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                        <option value="아키타" ${region == '아키타' ? 'selected' : ''}>아키타</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="style" class="form-label">스타일</label>
                    <select class="form-select" id="style" name="style">
                        <option value="">전체</option>
                        <optgroup label="준마이 계열">
                            <option value="준마이다이긴조" ${style == '준마이다이긴조' ? 'selected' : ''}>준마이다이긴조</option>
                            <option value="준마이긴조" ${style == '준마이긴조' ? 'selected' : ''}>준마이긴조</option>
                            <option value="준마이" ${style == '준마이' ? 'selected' : ''}>준마이</option>
                        </optgroup>
                        <optgroup label="혼조조 계열">
                            <option value="다이긴조" ${style == '다이긴조' ? 'selected' : ''}>다이긴조</option>
                            <option value="긴조" ${style == '긴조' ? 'selected' : ''}>긴조</option>
                            <option value="혼조조" ${style == '혼조조' ? 'selected' : ''}>혼조조</option>
                        </optgroup>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="keyword" class="form-label">검색</label>
                    <input type="text" class="form-control" id="keyword" name="keyword" 
                           value="${keyword}" placeholder="사케 이름, 브랜드 검색">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">검색</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 결과 카운트 -->
    <div class="mb-3">
        <p class="text-muted">총 <strong>${totalCount}</strong>개의 사케가 있습니다.</p>
    </div>
    
    <!-- 사케 목록 -->
    <c:choose>
        <c:when test="${not empty sakeList}">
            <div class="row">
                <c:forEach var="sake" items="${sakeList}">
                    <div class="col-md-3 mb-4">
                        <div class="card h-100">
                            <div class="card-img-top bg-light d-flex align-items-center justify-content-center" 
                                 style="height: 200px; overflow: hidden;">
                                <c:choose>
                                    <c:when test="${not empty sake.thumbnailPath}">
                                        <img src="${pageContext.request.contextPath}${sake.thumbnailPath}" 
                                             alt="${sake.displayName}" class="img-fluid" style="max-height: 100%;">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-wine-bottle fa-4x text-muted"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${sake.displayName}</h5>
                                <p class="card-text text-muted small mb-2">
                                    <c:if test="${not empty sake.brand}">
                                        <strong>${sake.brand}</strong><br>
                                    </c:if>
                                    <c:if test="${not empty sake.brewery}">
                                        ${sake.brewery}<br>
                                    </c:if>
                                    <c:if test="${not empty sake.regionPrefecture}">
                                        <i class="fas fa-map-marker-alt"></i> ${sake.regionPrefecture}
                                    </c:if>
                                </p>
                                <c:if test="${not empty sake.style}">
                                    <span class="badge bg-secondary">${sake.style}</span>
                                </c:if>
                            </div>
                            <div class="card-footer bg-white">
                                <a href="${pageContext.request.contextPath}/sake/detail?sakeId=${sake.sakeId}" 
                                   class="btn btn-primary btn-sm w-100">상세보기</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <!-- 페이징 -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="페이지 네비게이션">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage - 1}&region=${region}&style=${style}&keyword=${keyword}">이전</a>
                        </li>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}&region=${region}&style=${style}&keyword=${keyword}">${i}</a>
                                </li>
                            </c:if>
                            <c:if test="${i == currentPage - 3 || i == currentPage + 3}">
                                <li class="page-item disabled">
                                    <span class="page-link">...</span>
                                </li>
                            </c:if>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="?page=${currentPage + 1}&region=${region}&style=${style}&keyword=${keyword}">다음</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center py-5">
                <i class="fas fa-info-circle fa-3x mb-3"></i>
                <p>검색 결과가 없습니다.</p>
                <a href="${pageContext.request.contextPath}/sake/list" class="btn btn-primary">전체 목록 보기</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

