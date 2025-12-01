<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-3">
            <div class="card">
                <div class="card-body text-center">
                    <i class="fas fa-user-circle fa-5x text-primary mb-3"></i>
                    <h4>${sessionScope.user.fullName}</h4>
                    <p class="text-muted">@${sessionScope.user.username}</p>
                    <p class="text-muted">${sessionScope.user.email}</p>
                </div>
            </div>
        </div>
        
        <div class="col-md-9">
            <h2 class="mb-4">마이페이지</h2>
            
            <!-- 에러 메시지 -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>
            
            <!-- 사용자 정보 -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">내 정보</h5>
                </div>
                <div class="card-body">
                    <table class="table">
                        <tr>
                            <th width="150">사용자명</th>
                            <td>${sessionScope.user.username}</td>
                        </tr>
                        <tr>
                            <th>이메일</th>
                            <td>${sessionScope.user.email}</td>
                        </tr>
                        <tr>
                            <th>이름</th>
                            <td>${sessionScope.user.fullName}</td>
                        </tr>
                        <tr>
                            <th>역할</th>
                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.user.role == 'ADMIN'}">
                                        <span class="badge bg-danger">관리자</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-primary">일반 사용자</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>가입일</th>
                            <td>${sessionScope.user.createdAt}</td>
                        </tr>
                    </table>
                </div>
            </div>
            
            <!-- 내 주문 내역 -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0">주문 내역 (${orders.size()})</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty orders}">
                            <div class="list-group">
                                <c:forEach var="order" items="${orders}">
                                    <div class="list-group-item mb-3">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <div>
                                                <h6 class="mb-1">주문번호: #${order.orderId}</h6>
                                                <small class="text-muted">
                                                    주문일: ${order.createdAt}
                                                </small>
                                            </div>
                                            <div class="text-end">
                                                <span class="badge 
                                                    <c:choose>
                                                        <c:when test="${order.status == 'COMPLETED'}">bg-success</c:when>
                                                        <c:when test="${order.status == 'PROCESSING'}">bg-warning</c:when>
                                                        <c:when test="${order.status == 'CANCELLED'}">bg-danger</c:when>
                                                        <c:otherwise>bg-secondary</c:otherwise>
                                                    </c:choose>
                                                ">
                                                    <c:choose>
                                                        <c:when test="${order.status == 'COMPLETED'}">완료</c:when>
                                                        <c:when test="${order.status == 'PROCESSING'}">처리중</c:when>
                                                        <c:when test="${order.status == 'CANCELLED'}">취소</c:when>
                                                        <c:otherwise>대기중</c:otherwise>
                                                    </c:choose>
                                                </span>
                                                <div class="mt-1">
                                                    <strong class="text-primary">
                                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₩"/>
                                                    </strong>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <!-- 주문 상품 목록 -->
                                        <div class="mt-2">
                                            <small class="text-muted">주문 상품:</small>
                                            <ul class="list-unstyled mb-0 mt-1">
                                                <c:forEach var="item" items="${order.items}">
                                                    <li class="small">
                                                        <c:if test="${not empty item.product}">
                                                            ${item.product.sakeName != null ? item.product.sakeName : '상품명 없음'}
                                                        </c:if>
                                                        <c:if test="${empty item.product}">
                                                            상품 ID: ${item.productId}
                                                        </c:if>
                                                        × ${item.quantity}개
                                                        <span class="text-muted">
                                                            (<fmt:formatNumber value="${item.price * item.quantity}" type="currency" currencySymbol="₩"/>)
                                                        </span>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        
                                        <!-- 배송 정보 -->
                                        <div class="mt-2 pt-2 border-top">
                                            <small class="text-muted">
                                                <i class="fas fa-user"></i> ${order.recipientName} | 
                                                <i class="fas fa-phone"></i> ${order.recipientPhone}<br>
                                                <i class="fas fa-map-marker-alt"></i> ${order.shippingAddress}
                                            </small>
                                        </div>
                                        
                                        <div class="mt-2">
                                            <a href="${pageContext.request.contextPath}/order/detail?orderId=${order.orderId}" 
                                               class="btn btn-sm btn-outline-primary">상세보기</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted text-center py-4">주문 내역이 없습니다.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <!-- 내 리뷰 목록 -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">내 리뷰 (${reviews.size()})</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty reviews}">
                            <div class="list-group">
                                <c:forEach var="review" items="${reviews}">
                                    <div class="list-group-item">
                                        <div class="d-flex justify-content-between align-items-start">
                                            <div class="flex-grow-1">
                                                <h6 class="mb-1">${review.title}</h6>
                                                <p class="mb-1">${review.content}</p>
                                                <small class="text-muted">
                                                    <c:forEach begin="1" end="${review.rating}">
                                                        <i class="fas fa-star text-warning"></i>
                                                    </c:forEach>
                                                    <c:forEach begin="${review.rating + 1}" end="5">
                                                        <i class="far fa-star text-warning"></i>
                                                    </c:forEach>
                                                    | ${review.createdAt}
                                                </small>
                                            </div>
                                            <div>
                                                <a href="${pageContext.request.contextPath}/review/edit?reviewId=${review.reviewId}" 
                                                   class="btn btn-sm btn-outline-primary">수정</a>
                                                <a href="${pageContext.request.contextPath}/review/delete?reviewId=${review.reviewId}" 
                                                   class="btn btn-sm btn-outline-danger" 
                                                   onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted text-center py-4">작성한 리뷰가 없습니다.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

