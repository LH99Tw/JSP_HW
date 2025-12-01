<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/i18n_init.jspf" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-store"></i> <fmt:message key="product.market.title" bundle="${messages}"/></h2>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/product/list" class="row g-3">
                <div class="col-md-3">
                    <label for="region" class="form-label"><fmt:message key="product.filter.region" bundle="${messages}"/></label>
                    <select class="form-select" id="region" name="region">
                        <option value=""><fmt:message key="product.filter.all" bundle="${messages}"/></option>
                        <option value="후쿠시마" ${region == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                        <option value="아키타" ${region == '아키타' ? 'selected' : ''}>아키타</option>
                        <option value="야마가타" ${region == '야마가타' ? 'selected' : ''}>야마가타</option>
                        <option value="니가타" ${region == '니가타' ? 'selected' : ''}>니가타</option>
                        <option value="효고" ${region == '효고' ? 'selected' : ''}>효고</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="style" class="form-label"><fmt:message key="product.filter.style" bundle="${messages}"/></label>
                    <select class="form-select" id="style" name="style">
                        <option value=""><fmt:message key="product.filter.all" bundle="${messages}"/></option>
                        <optgroup label="<fmt:message key="sake.classification.polishing" bundle="${messages}"/> - <fmt:message key="sake.classification.junmai" bundle="${messages}"/>">
                            <option value="준마이다이긴조" ${style == '준마이다이긴조' ? 'selected' : ''}>준마이다이긴조</option>
                            <option value="준마이긴조" ${style == '준마이긴조' ? 'selected' : ''}>준마이긴조</option>
                            <option value="준마이" ${style == '준마이' ? 'selected' : ''}>준마이</option>
                        </optgroup>
                        <optgroup label="<fmt:message key="sake.classification.polishing" bundle="${messages}"/> - <fmt:message key="sake.classification.honjozo" bundle="${messages}"/>">
                            <option value="다이긴조" ${style == '다이긴조' ? 'selected' : ''}>다이긴조</option>
                            <option value="긴조" ${style == '긴조' ? 'selected' : ''}>긴조</option>
                            <option value="혼조조" ${style == '혼조조' ? 'selected' : ''}>혼조조</option>
                        </optgroup>
                        <optgroup label="<fmt:message key="sake.classification.brewing" bundle="${messages}"/>">
                            <option value="나마자케" ${style == '나마자케' ? 'selected' : ''}>나마자케</option>
                            <option value="나마초조" ${style == '나마초조' ? 'selected' : ''}>나마초조</option>
                            <option value="나마즈메" ${style == '나마즈메' ? 'selected' : ''}>나마즈메</option>
                            <option value="니고리자케" ${style == '니고리자케' ? 'selected' : ''}>니고리자케</option>
                            <option value="키죠슈" ${style == '키죠슈' ? 'selected' : ''}>키죠슈</option>
                        </optgroup>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="search" class="form-label"><fmt:message key="common.search" bundle="${messages}"/></label>
                    <input type="text" class="form-control" id="search" name="search" 
                           value="${search}" placeholder="<fmt:message key="product.search.placeholder" bundle="${messages}"/>">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i> <fmt:message key="product.search.button" bundle="${messages}"/>
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 상품 목록 -->
    <div class="row g-3 product-list">
        <c:choose>
            <c:when test="${empty products}">
                <div class="col-12">
                    <div class="alert alert-info text-center" role="alert">
                        <i class="fas fa-info-circle"></i> <fmt:message key="product.empty" bundle="${messages}"/>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="product" items="${products}">
                    <div class="col-6 col-sm-4 col-md-2 col-lg-2 mb-4">
                        <div class="card h-100">
                            <div class="card-img-top bg-light d-flex align-items-center justify-content-center" 
                                 style="height: 300px; overflow: hidden; position: relative;">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${pageContext.request.contextPath}${product.imageUrl}" 
                                             class="card-img-top" 
                                             alt="${product.sakeName}" 
                                             style="width: 100%; height: 100%; object-fit: cover; object-position: center;">
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-wine-bottle fa-4x text-muted"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="card-body d-flex flex-column">
                                <h6 class="card-title">${product.sakeName}</h6>
                                <p class="card-text text-muted small mb-2">
                                    ${product.brand}<br>
                                    <c:if test="${not empty product.region}">
                                        <i class="fas fa-map-marker-alt"></i> ${product.region}
                                    </c:if>
                                </p>
                                <div class="mt-auto">
                                    <div class="d-flex flex-column mb-2">
                                        <span class="h6 text-primary mb-1">
                                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₩"/>
                                        </span>
                                        <c:if test="${product.stock > 0}">
                                            <span class="badge bg-success small"><fmt:message key="product.in_stock" bundle="${messages}"/></span>
                                        </c:if>
                                        <c:if test="${product.stock <= 0}">
                                            <span class="badge bg-danger small"><fmt:message key="product.sold_out" bundle="${messages}"/></span>
                                        </c:if>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/product/detail?productId=${product.productId}" 
                                       class="btn btn-primary btn-sm w-100">
                                        <i class="fas fa-eye"></i> <fmt:message key="product.detail.view" bundle="${messages}"/>
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

