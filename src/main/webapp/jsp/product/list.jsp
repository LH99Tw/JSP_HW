<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-store"></i> 사케 마켓</h2>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/product/list" class="row g-3">
                <div class="col-md-3">
                    <label for="region" class="form-label">지역</label>
                    <select class="form-select" id="region" name="region">
                        <option value="">전체</option>
                        <option value="후쿠시마" ${region == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                        <option value="아키타" ${region == '아키타' ? 'selected' : ''}>아키타</option>
                        <option value="야마가타" ${region == '야마가타' ? 'selected' : ''}>야마가타</option>
                        <option value="니가타" ${region == '니가타' ? 'selected' : ''}>니가타</option>
                        <option value="효고" ${region == '효고' ? 'selected' : ''}>효고</option>
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
                    <label for="search" class="form-label">검색</label>
                    <input type="text" class="form-control" id="search" name="search" 
                           value="${search}" placeholder="상품명 또는 브랜드 검색">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i> 검색
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 상품 목록 -->
    <div class="row">
        <c:choose>
            <c:when test="${empty products}">
                <div class="col-12">
                    <div class="alert alert-info text-center" role="alert">
                        <i class="fas fa-info-circle"></i> 등록된 상품이 없습니다.
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="product" items="${products}">
                    <div class="col-md-4 mb-4">
                        <div class="card h-100">
                            <c:if test="${not empty product.imageUrl}">
                                <img src="${product.imageUrl}" class="card-img-top" 
                                     alt="${product.sakeName}" style="height: 200px; object-fit: cover;">
                            </c:if>
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">${product.sakeName}</h5>
                                <p class="card-text text-muted small">
                                    ${product.brand} · ${product.region}
                                </p>
                                <div class="mt-auto">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span class="h5 text-primary mb-0">
                                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₩"/>
                                        </span>
                                        <c:if test="${product.stock > 0}">
                                            <span class="badge bg-success">재고 있음</span>
                                        </c:if>
                                        <c:if test="${product.stock <= 0}">
                                            <span class="badge bg-danger">품절</span>
                                        </c:if>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/product/detail?productId=${product.productId}" 
                                       class="btn btn-primary w-100">
                                        <i class="fas fa-eye"></i> 상세보기
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

