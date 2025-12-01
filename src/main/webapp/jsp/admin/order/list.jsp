<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-shopping-bag"></i> 주문 관리</h2>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/admin/order/list" class="row g-3">
                <div class="col-md-3">
                    <label for="userId" class="form-label">사용자 ID</label>
                    <input type="number" class="form-control" id="userId" name="userId" 
                           value="${userId}" placeholder="사용자 ID">
                </div>
                <div class="col-md-3">
                    <label for="status" class="form-label">주문 상태</label>
                    <select class="form-select" id="status" name="status">
                        <option value="">전체</option>
                        <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>주문 대기</option>
                        <option value="PROCESSING" ${status == 'PROCESSING' ? 'selected' : ''}>처리 중</option>
                        <option value="COMPLETED" ${status == 'COMPLETED' ? 'selected' : ''}>완료</option>
                        <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>취소</option>
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
    
    <!-- 주문 목록 -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>주문번호</th>
                            <th>사용자 ID</th>
                            <th>받는 분</th>
                            <th>총 금액</th>
                            <th>상태</th>
                            <th>주문일</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty orders}">
                                <tr>
                                    <td colspan="7" class="text-center py-4">등록된 주문이 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td>#${order.orderId}</td>
                                        <td>${order.userId}</td>
                                        <td>${order.recipientName}</td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₩"/>
                                            </strong>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${order.status == 'PENDING'}">
                                                    <span class="badge bg-warning text-dark">주문 대기</span>
                                                </c:when>
                                                <c:when test="${order.status == 'PROCESSING'}">
                                                    <span class="badge bg-info">처리 중</span>
                                                </c:when>
                                                <c:when test="${order.status == 'COMPLETED'}">
                                                    <span class="badge bg-success">완료</span>
                                                </c:when>
                                                <c:when test="${order.status == 'CANCELLED'}">
                                                    <span class="badge bg-danger">취소</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${order.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <%
                                                com.example.model.Order orderObj = (com.example.model.Order) pageContext.getAttribute("order");
                                                if (orderObj != null && orderObj.getCreatedAt() != null) {
                                                    java.util.Date date = new java.util.Date(orderObj.getCreatedAt().getTime());
                                                    pageContext.setAttribute("orderDate", date);
                                                }
                                            %>
                                            <c:if test="${not empty orderDate}">
                                                <fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd HH:mm"/>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/order/detail?orderId=${order.orderId}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-eye"></i> 상세
                                            </a>
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

