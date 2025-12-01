<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-star"></i> 리뷰 관리</h2>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/admin/review/list" class="row g-3">
                <div class="col-md-3">
                    <label for="sakeId" class="form-label">사케 ID</label>
                    <input type="number" class="form-control" id="sakeId" name="sakeId" 
                           value="${sakeId}" placeholder="사케 ID">
                </div>
                <div class="col-md-3">
                    <label for="userId" class="form-label">사용자 ID</label>
                    <input type="number" class="form-control" id="userId" name="userId" 
                           value="${userId}" placeholder="사용자 ID">
                </div>
                <div class="col-md-3">
                    <label for="rating" class="form-label">평점</label>
                    <select class="form-select" id="rating" name="rating">
                        <option value="">전체</option>
                        <option value="5" ${rating == '5' ? 'selected' : ''}>5점</option>
                        <option value="4" ${rating == '4' ? 'selected' : ''}>4점</option>
                        <option value="3" ${rating == '3' ? 'selected' : ''}>3점</option>
                        <option value="2" ${rating == '2' ? 'selected' : ''}>2점</option>
                        <option value="1" ${rating == '1' ? 'selected' : ''}>1점</option>
                    </select>
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i> 검색
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 리뷰 목록 -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>사케 ID</th>
                            <th>사용자 ID</th>
                            <th>평점</th>
                            <th>내용</th>
                            <th>작성일</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty reviews}">
                                <tr>
                                    <td colspan="7" class="text-center py-4">등록된 리뷰가 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="review" items="${reviews}">
                                    <tr>
                                        <td>${review.reviewId}</td>
                                        <td>${review.sakeId}</td>
                                        <td>${review.userId}</td>
                                        <td>
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="fas fa-star ${i <= review.rating ? 'text-warning' : 'text-muted'}"></i>
                                            </c:forEach>
                                            <span class="ms-1">${review.rating}</span>
                                        </td>
                                        <td>
                                            <div style="max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                                ${review.content}
                                            </div>
                                        </td>
                                        <td>
                                            <%
                                                if (review.getCreatedAt() != null) {
                                                    java.util.Date date = new java.util.Date(review.getCreatedAt().getTime());
                                                    request.setAttribute("reviewDate", date);
                                                }
                                            %>
                                            <c:if test="${not empty reviewDate}">
                                                <fmt:formatDate value="${reviewDate}" pattern="yyyy-MM-dd HH:mm"/>
                                            </c:if>
                                        </td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/review/delete" 
                                                  style="display: inline;" 
                                                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                                <input type="hidden" name="reviewId" value="${review.reviewId}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                    <i class="fas fa-trash"></i> 삭제
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

