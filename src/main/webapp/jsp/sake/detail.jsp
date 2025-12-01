<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 브레드크럼 -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">홈</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sake/list">커뮤니티</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sake/list">사케 목록</a></li>
            <li class="breadcrumb-item active">${sake.displayName}</li>
        </ol>
    </nav>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <c:if test="${not empty sake}">
        <div class="row mb-4">
            <!-- 사케 이미지 -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body text-center p-4">
                        <c:choose>
                            <c:when test="${not empty sake.thumbnailPath}">
                                <img src="${pageContext.request.contextPath}${sake.thumbnailPath}" 
                                     alt="${sake.displayName}" class="img-fluid" style="max-height: 400px;">
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-wine-bottle fa-5x text-muted"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- 사케 정보 -->
            <div class="col-md-8">
                <h1 class="mb-3">${sake.displayName}</h1>
                
                <div class="mb-3">
                    <c:if test="${not empty sake.brand}">
                        <p class="mb-1"><strong>브랜드:</strong> ${sake.brand}</p>
                    </c:if>
                    <c:if test="${not empty sake.brewery}">
                        <p class="mb-1"><strong>주조장:</strong> ${sake.brewery}</p>
                    </c:if>
                    <c:if test="${not empty sake.regionPrefecture}">
                        <p class="mb-1"><strong>원산지:</strong> ${sake.regionPrefecture}</p>
                    </c:if>
                    <c:if test="${not empty sake.style}">
                        <p class="mb-1"><strong>스타일:</strong> <span class="badge bg-secondary">${sake.style}</span></p>
                    </c:if>
                </div>
                
                <!-- 평점 표시 -->
                <div class="mb-3">
                    <%
                        Double avgRating = (Double) request.getAttribute("averageRating");
                        int reviewCount = (Integer) request.getAttribute("reviewCount");
                    %>
                    <div class="d-flex align-items-center">
                        <span class="display-6 me-3">
                            <fmt:formatNumber value="${averageRating}" pattern="#.#"/>
                        </span>
                        <div>
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= averageRating}">
                                        <i class="fas fa-star text-warning"></i>
                                    </c:when>
                                    <c:when test="${i - 0.5 <= averageRating}">
                                        <i class="fas fa-star-half-alt text-warning"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star text-warning"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <span class="text-muted ms-2">(${reviewCount}개 리뷰)</span>
                        </div>
                    </div>
                </div>
                
                <!-- 기본 정보 테이블 -->
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">기본 정보</h5>
                        <table class="table table-sm">
                            <c:if test="${not empty sake.alcoholPercent}">
                                <tr>
                                    <th width="150">알코올 도수</th>
                                    <td>${sake.alcoholPercent}%</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty sake.volumeMl}">
                                <tr>
                                    <th>용량</th>
                                    <td>${sake.volumeMl}ml</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty sake.polishingRatio}">
                                <tr>
                                    <th>정미율</th>
                                    <td>${sake.polishingRatio}%</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty sake.nihonshuDo}">
                                <tr>
                                    <th>일본주도</th>
                                    <td>${sake.nihonshuDo}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty sake.acidity}">
                                <tr>
                                    <th>산도</th>
                                    <td>${sake.acidity}</td>
                                </tr>
                            </c:if>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 맛 프로파일 -->
        <c:if test="${not empty sake.sweetnessLevel || not empty sake.bodyLevel}">
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">맛 프로파일</h5>
                </div>
                <div class="card-body">
                    <c:if test="${not empty sake.sweetnessLevel}">
                        <div class="mb-3">
                            <label class="form-label">단맛 (1=카라구치, 5=아마구치)</label>
                            <div class="progress" style="height: 25px;">
                                <div class="progress-bar" role="progressbar" 
                                     style="width: ${sake.sweetnessLevel * 20}%"
                                     aria-valuenow="${sake.sweetnessLevel}" aria-valuemin="1" aria-valuemax="5">
                                    ${sake.sweetnessLevel}/5
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty sake.bodyLevel}">
                        <div class="mb-3">
                            <label class="form-label">바디감 (1=라이트, 5=풀바디)</label>
                            <div class="progress" style="height: 25px;">
                                <div class="progress-bar bg-success" role="progressbar" 
                                     style="width: ${sake.bodyLevel * 20}%"
                                     aria-valuenow="${sake.bodyLevel}" aria-valuemin="1" aria-valuemax="5">
                                    ${sake.bodyLevel}/5
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty sake.aromaType}">
                        <p><strong>향기 타입:</strong> ${sake.aromaType}</p>
                    </c:if>
                </div>
            </div>
        </c:if>
        
        <!-- 리뷰 섹션 -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">리뷰 (${reviewCount})</h5>
                <c:if test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/review/form?sakeId=${sake.sakeId}" 
                       class="btn btn-primary btn-sm">
                        <i class="fas fa-pen"></i> 리뷰 작성
                    </a>
                </c:if>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <c:forEach var="review" items="${reviews}">
                            <div class="border-bottom pb-3 mb-3">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <div>
                                        <strong>${review.title != null ? review.title : '제목 없음'}</strong>
                                        <div class="mt-1">
                                            <c:forEach begin="1" end="5" var="i">
                                                <c:if test="${i <= review.rating}">
                                                    <i class="fas fa-star text-warning"></i>
                                                </c:if>
                                                <c:if test="${i > review.rating}">
                                                    <i class="far fa-star text-warning"></i>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${review.createdAt}" pattern="yyyy.MM.dd"/>
                                    </small>
                                </div>
                                <p class="mb-2">${review.content}</p>
                                <c:if test="${not empty sessionScope.user && sessionScope.user.userId == review.userId}">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/review/edit?reviewId=${review.reviewId}" 
                                           class="btn btn-sm btn-outline-primary">수정</a>
                                        <a href="${pageContext.request.contextPath}/review/delete?reviewId=${review.reviewId}" 
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted text-center py-4">아직 리뷰가 없습니다.</p>
                        <c:if test="${not empty sessionScope.user}">
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/review/form?sakeId=${sake.sakeId}" 
                                   class="btn btn-primary">
                                    첫 리뷰 작성하기
                                </a>
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

