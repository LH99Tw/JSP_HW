<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-shopping-bag"></i> 주문 상세</h2>
    
    <div class="row">
        <div class="col-md-8">
            <!-- 주문 상품 목록 -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">주문 상품</h5>
                </div>
                <div class="card-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>상품</th>
                                <th>가격</th>
                                <th>수량</th>
                                <th>소계</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>
                                        <c:if test="${not empty item.product}">
                                            <strong>${item.product.sakeName}</strong><br>
                                            <small class="text-muted">${item.product.brand}</small>
                                        </c:if>
                                        <c:if test="${empty item.product}">
                                            상품 ID: ${item.productId}
                                        </c:if>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₩"/>
                                    </td>
                                    <td>${item.quantity}</td>
                                    <td>
                                        <strong>
                                            <fmt:formatNumber value="${item.price * item.quantity}" type="currency" currencySymbol="₩"/>
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
            <!-- 주문 정보 -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">주문 정보</h5>
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-5">주문번호</dt>
                        <dd class="col-sm-7">#${order.orderId}</dd>
                        
                        <dt class="col-sm-5">사용자 ID</dt>
                        <dd class="col-sm-7">${order.userId}</dd>
                        
                        <dt class="col-sm-5">받는 분</dt>
                        <dd class="col-sm-7">${order.recipientName}</dd>
                        
                        <dt class="col-sm-5">연락처</dt>
                        <dd class="col-sm-7">${order.recipientPhone}</dd>
                        
                        <dt class="col-sm-5">배송 주소</dt>
                        <dd class="col-sm-7">
                            ${order.shippingAddress}
                            <c:if test="${not empty order.shippingAddressDetail}">
                                ${order.shippingAddressDetail}
                            </c:if>
                        </dd>
                        
                        <dt class="col-sm-5">주문 상태</dt>
                        <dd class="col-sm-7">
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
                        </dd>
                        
                        <dt class="col-sm-5">총 금액</dt>
                        <dd class="col-sm-7">
                            <strong class="text-primary">
                                <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₩"/>
                            </strong>
                        </dd>
                        
                        <dt class="col-sm-5">주문일</dt>
                        <dd class="col-sm-7">
                            <%
                                if (order.getCreatedAt() != null) {
                                    java.util.Date date = new java.util.Date(order.getCreatedAt().getTime());
                                    request.setAttribute("orderDate", date);
                                }
                            %>
                            <c:if test="${not empty orderDate}">
                                <fmt:formatDate value="${orderDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </dd>
                    </dl>
                </div>
            </div>
            
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/admin/order/list" class="btn btn-secondary w-100">
                    <i class="fas fa-arrow-left"></i> 목록으로
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

