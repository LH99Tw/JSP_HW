<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <div class="row">
        <!-- 상품 이미지 -->
        <div class="col-md-6">
            <c:if test="${not empty product.imageUrl}">
                <img src="${product.imageUrl}" class="img-fluid rounded" alt="${product.sakeName}" style="max-height: 500px; width: 100%; object-fit: cover;">
            </c:if>
            <c:if test="${empty product.imageUrl}">
                <div class="bg-light d-flex align-items-center justify-content-center rounded" style="height: 500px;">
                    <i class="fas fa-wine-bottle fa-5x text-muted"></i>
                </div>
            </c:if>
        </div>
        
        <!-- 상품 정보 -->
        <div class="col-md-6">
            <h2 class="mb-3">${product.sakeName}</h2>
            <p class="text-muted mb-3">
                <strong>브랜드:</strong> ${product.brand}<br>
                <strong>지역:</strong> ${product.region}<br>
                <strong>스타일:</strong> ${product.style}
            </p>
            
            <c:if test="${not empty product.label}">
                <span class="badge bg-warning text-dark mb-3">${product.label}</span>
            </c:if>
            
            <div class="border-top pt-3 mb-3">
                <h3 class="text-primary mb-3">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₩"/>
                </h3>
                
                <div class="mb-3">
                    <c:if test="${product.stock > 0}">
                        <span class="badge bg-success fs-6">재고 있음 (${product.stock}개)</span>
                    </c:if>
                    <c:if test="${product.stock <= 0}">
                        <span class="badge bg-danger fs-6">품절</span>
                    </c:if>
                </div>
                
                <c:if test="${product.stock > 0}">
                    <form method="post" action="${pageContext.request.contextPath}/cart/add" class="mb-3">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <div class="row mb-3">
                            <div class="col-4">
                                <label for="quantity" class="form-label">수량</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" 
                                       value="1" min="1" max="${product.stock}" required>
                            </div>
                        </div>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <button type="submit" class="btn btn-primary btn-lg w-100">
                                    <i class="fas fa-shopping-cart"></i> 장바구니에 담기
                                </button>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg w-100">
                                    <i class="fas fa-sign-in-alt"></i> 로그인 후 구매하기
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </c:if>
            </div>
            
            <!-- 사케 상세 정보 -->
            <c:if test="${not empty sake}">
                <div class="card mt-4">
                    <div class="card-header">
                        <h5 class="mb-0">사케 정보</h5>
                    </div>
                    <div class="card-body">
                        <dl class="row mb-0">
                            <dt class="col-sm-4">양조장</dt>
                            <dd class="col-sm-8">${sake.brewery}</dd>
                            
                            <c:if test="${not empty sake.alcoholPercent}">
                                <dt class="col-sm-4">도수</dt>
                                <dd class="col-sm-8">${sake.alcoholPercent}%</dd>
                            </c:if>
                            
                            <c:if test="${not empty sake.volumeMl}">
                                <dt class="col-sm-4">용량</dt>
                                <dd class="col-sm-8">${sake.volumeMl}ml</dd>
                            </c:if>
                            
                            <c:if test="${not empty sake.polishingRatio}">
                                <dt class="col-sm-4">정미율</dt>
                                <dd class="col-sm-8">${sake.polishingRatio}%</dd>
                            </c:if>
                        </dl>
                        <a href="${pageContext.request.contextPath}/sake/detail?sakeId=${sake.sakeId}" 
                           class="btn btn-outline-primary btn-sm">
                            <i class="fas fa-info-circle"></i> 사케 상세 정보 보기
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

