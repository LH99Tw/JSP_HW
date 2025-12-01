<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-chart-line"></i> 관리자 대시보드</h2>
    
    <!-- 통계 카드 -->
    <div class="row mb-4">
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-primary">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-0">사케 수</h6>
                            <h3 class="mb-0">${sakeCount}</h3>
                        </div>
                        <i class="fas fa-wine-bottle fa-3x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-success">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-0">리뷰 수</h6>
                            <h3 class="mb-0">${reviewCount}</h3>
                        </div>
                        <i class="fas fa-star fa-3x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-info">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-0">상품 수</h6>
                            <h3 class="mb-0">${productCount}</h3>
                        </div>
                        <i class="fas fa-store fa-3x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card text-white bg-warning">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title mb-0">오늘 주문</h6>
                            <h3 class="mb-0">${todayOrderCount}</h3>
                        </div>
                        <i class="fas fa-shopping-bag fa-3x opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 인기 사케 TOP 10 -->
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0"><i class="fas fa-trophy"></i> 인기 사케 TOP 10 (리뷰 수 기준)</h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty popularSakes}">
                    <p class="text-muted text-center">인기 사케 데이터가 없습니다.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>순위</th>
                                    <th>사케명</th>
                                    <th>브랜드</th>
                                    <th>지역</th>
                                    <th>스타일</th>
                                    <th>리뷰 수</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="sake" items="${popularSakes}" varStatus="status">
                                    <tr>
                                        <td>
                                            <span class="badge bg-primary">${status.index + 1}</span>
                                        </td>
                                        <td>${sake.nameKo}</td>
                                        <td>${sake.brand}</td>
                                        <td>${sake.regionPrefecture}</td>
                                        <td><span class="badge bg-secondary">${sake.style}</span></td>
                                        <td>
                                            <%
                                                // 리뷰 수는 별도로 조회해야 함 (간단히 표시)
                                            %>
                                            <span class="text-muted">-</span>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/sake/form?sakeId=${sake.sakeId}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-edit"></i> 수정
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

