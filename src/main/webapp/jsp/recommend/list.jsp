<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4">
        <i class="fas fa-star"></i> 
        <c:choose>
            <c:when test="${isRecommended}">
                추천 사케
            </c:when>
            <c:otherwise>
                인기 사케
            </c:otherwise>
        </c:choose>
    </h2>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${empty sakes}">
            <div class="alert alert-info text-center" role="alert">
                <i class="fas fa-info-circle"></i> 추천할 사케가 없습니다.
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <c:forEach var="sake" items="${sakes}">
                    <div class="col-md-4 mb-4">
                        <div class="card h-100">
                            <c:if test="${not empty sake.thumbnailPath}">
                                <img src="${sake.thumbnailPath}" class="card-img-top" 
                                     alt="${sake.nameKo}" style="height: 200px; object-fit: cover;">
                            </c:if>
                            <div class="card-body">
                                <h5 class="card-title">${sake.nameKo}</h5>
                                <p class="card-text">
                                    <c:if test="${not empty sake.brand}">
                                        <strong>브랜드:</strong> ${sake.brand}<br>
                                    </c:if>
                                    <c:if test="${not empty sake.regionPrefecture}">
                                        <strong>지역:</strong> ${sake.regionPrefecture}<br>
                                    </c:if>
                                    <c:if test="${not empty sake.style}">
                                        <strong>스타일:</strong> <span class="badge bg-secondary">${sake.style}</span><br>
                                    </c:if>
                                    <c:if test="${not empty sake.alcoholPercent}">
                                        <strong>도수:</strong> ${sake.alcoholPercent}%
                                    </c:if>
                                </p>
                            </div>
                            <div class="card-footer">
                                <a href="${pageContext.request.contextPath}/sake/detail?sakeId=${sake.sakeId}" 
                                   class="btn btn-primary w-100">
                                    <i class="fas fa-info-circle"></i> 상세보기
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

