<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card text-center">
                <div class="card-body py-5">
                    <div class="mb-4">
                        <i class="fas fa-check-circle fa-5x text-success"></i>
                    </div>
                    <h2 class="mb-3">주문이 완료되었습니다!</h2>
                    <p class="text-muted mb-4">
                        주문번호: <strong>#${order.orderId}</strong>
                    </p>
                    <p class="mb-4">
                        주문해주셔서 감사합니다. 주문 내역은 마이페이지에서 확인하실 수 있습니다.
                    </p>
                    
                    <div class="card bg-light mb-4">
                        <div class="card-body text-start">
                            <h5 class="card-title mb-3">주문 정보</h5>
                            <dl class="row mb-0">
                                <dt class="col-sm-4">주문번호</dt>
                                <dd class="col-sm-8">#${order.orderId}</dd>
                                
                                <dt class="col-sm-4">받는 분</dt>
                                <dd class="col-sm-8">${order.recipientName}</dd>
                                
                                <dt class="col-sm-4">연락처</dt>
                                <dd class="col-sm-8">${order.recipientPhone}</dd>
                                
                                <dt class="col-sm-4">배송 주소</dt>
                                <dd class="col-sm-8">
                                    ${order.shippingAddress}
                                    <c:if test="${not empty order.shippingAddressDetail}">
                                        ${order.shippingAddressDetail}
                                    </c:if>
                                </dd>
                                
                                <dt class="col-sm-4">총 결제금액</dt>
                                <dd class="col-sm-8">
                                    <strong class="text-primary">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₩"/>
                                    </strong>
                                </dd>
                                
                                <dt class="col-sm-4">주문 상태</dt>
                                <dd class="col-sm-8">
                                    <span class="badge bg-warning text-dark">주문 대기</span>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    
                    <div class="d-flex justify-content-center gap-2">
                        <a href="${pageContext.request.contextPath}/mypage" class="btn btn-primary">
                            <i class="fas fa-user"></i> 마이페이지
                        </a>
                        <a href="${pageContext.request.contextPath}/product/list" class="btn btn-outline-primary">
                            <i class="fas fa-shopping-bag"></i> 쇼핑 계속하기
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

