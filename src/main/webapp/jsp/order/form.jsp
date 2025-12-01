<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-credit-card"></i> 주문하기</h2>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/order/create">
        <div class="row">
            <!-- 주문 상품 목록 -->
            <div class="col-md-8">
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
                                <c:forEach var="item" items="${cartItems}">
                                    <tr>
                                        <td>
                                            <strong>${item.product.sakeName}</strong><br>
                                            <small class="text-muted">${item.product.brand} · ${item.product.region}</small>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₩"/>
                                        </td>
                                        <td>${item.cartItem.quantity}</td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${item.product.price * item.cartItem.quantity}" type="currency" currencySymbol="₩"/>
                                            </strong>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <!-- 배송 정보 입력 -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">배송 정보</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="recipientName" class="form-label">받는 분 이름 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="recipientName" name="recipientName" 
                                   value="${user.fullName}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="recipientPhone" class="form-label">연락처 <span class="text-danger">*</span></label>
                            <input type="tel" class="form-control" id="recipientPhone" name="recipientPhone" 
                                   placeholder="010-1234-5678" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="shippingPostcode" class="form-label">우편번호</label>
                            <input type="text" class="form-control" id="shippingPostcode" name="shippingPostcode" 
                                   placeholder="12345">
                        </div>
                        
                        <div class="mb-3">
                            <label for="shippingAddress" class="form-label">주소 <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="shippingAddress" name="shippingAddress" 
                                   placeholder="서울시 강남구 테헤란로 123" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="shippingAddressDetail" class="form-label">상세 주소</label>
                            <input type="text" class="form-control" id="shippingAddressDetail" name="shippingAddressDetail" 
                                   placeholder="101동 101호">
                        </div>
                        
                        <div class="mb-3">
                            <label for="memo" class="form-label">배송 메모</label>
                            <textarea class="form-control" id="memo" name="memo" rows="3" 
                                      placeholder="배송 시 요청사항을 입력해주세요."></textarea>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 주문 요약 -->
            <div class="col-md-4">
                <div class="card sticky-top" style="top: 20px;">
                    <div class="card-header">
                        <h5 class="mb-0">주문 요약</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between mb-3">
                            <span>총 상품 금액</span>
                            <strong>
                                <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₩"/>
                            </strong>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span>배송비</span>
                            <span>무료</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between mb-3">
                            <strong>총 결제금액</strong>
                            <strong class="text-primary fs-5">
                                <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₩"/>
                            </strong>
                        </div>
                        <button type="submit" class="btn btn-primary w-100 btn-lg">
                            <i class="fas fa-check"></i> 주문하기
                        </button>
                        <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary w-100 mt-2">
                            <i class="fas fa-arrow-left"></i> 장바구니로 돌아가기
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

