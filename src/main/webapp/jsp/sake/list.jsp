<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/i18n_init.jspf" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 브레드크럼 -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/"><fmt:message key="common.home" bundle="${messages}"/></a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sake/list"><fmt:message key="common.community" bundle="${messages}"/></a></li>
            <li class="breadcrumb-item active"><fmt:message key="sake.list.title" bundle="${messages}"/></li>
        </ol>
    </nav>
    
    <h1 class="mb-4"><fmt:message key="sake.list.title" bundle="${messages}"/></h1>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/sake/list" class="row g-3">
                <div class="col-md-3">
                    <label for="region" class="form-label"><fmt:message key="sake.filter.region" bundle="${messages}"/></label>
                    <select class="form-select" id="region" name="region">
                        <option value=""><fmt:message key="sake.filter.all" bundle="${messages}"/></option>
                        <option value="홋카이도" ${region == '홋카이도' ? 'selected' : ''}>홋카이도</option>
                        <option value="니가타" ${region == '니가타' ? 'selected' : ''}>니가타</option>
                        <option value="야마가타" ${region == '야마가타' ? 'selected' : ''}>야마가타</option>
                        <option value="후쿠시마" ${region == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                        <option value="아키타" ${region == '아키타' ? 'selected' : ''}>아키타</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="style" class="form-label"><fmt:message key="sake.filter.style" bundle="${messages}"/></label>
                    <select class="form-select" id="style" name="style">
                        <option value=""><fmt:message key="sake.filter.all" bundle="${messages}"/></option>
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
                    <label for="keyword" class="form-label"><fmt:message key="common.search" bundle="${messages}"/></label>
                    <input type="text" class="form-control" id="keyword" name="keyword" 
                           value="${keyword}" placeholder="<fmt:message key="sake.search.placeholder" bundle="${messages}"/>">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100"><fmt:message key="sake.search.button" bundle="${messages}"/></button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 결과 카운트 -->
    <div class="mb-3">
        <p class="text-muted"><fmt:message key="sake.count" bundle="${messages}"><fmt:param value="${totalCount}"/></fmt:message></p>
    </div>
    
    <!-- 사케 목록 -->
    <c:choose>
        <c:when test="${not empty sakeList}">
            <div class="row g-3 sake-list">
                <c:forEach var="sake" items="${sakeList}">
                    <div class="col-6 col-sm-4 col-md-2 col-lg-2 mb-4">
                        <div class="card h-100">
                            <div class="card-img-top bg-light d-flex align-items-center justify-content-center" 
                                 style="height: 300px; overflow: hidden; position: relative;">
                                <c:choose>
                                    <c:when test="${not empty sake.thumbnailPath}">
                                        <img src="${pageContext.request.contextPath}${sake.thumbnailPath}" 
                                             alt="${sake.displayName}" 
                                             class="img-fluid" 
                                             style="width: 100%; height: 100%; object-fit: cover; object-position: center;">
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
                                   class="btn btn-primary btn-sm w-100"><fmt:message key="sake.detail.view" bundle="${messages}"/></a>
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
                <p><fmt:message key="sake.no_results" bundle="${messages}"/></p>
                <a href="${pageContext.request.contextPath}/sake/list" class="btn btn-primary"><fmt:message key="sake.view_all" bundle="${messages}"/></a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

