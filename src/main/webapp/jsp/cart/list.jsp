<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-shopping-cart"></i> 장바구니</h2>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${empty cartItems}">
            <div class="alert alert-info text-center" role="alert">
                <i class="fas fa-info-circle"></i> 장바구니가 비어있습니다.
                <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary ms-3">
                    상품 보러가기
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>상품</th>
                                        <th>가격</th>
                                        <th>수량</th>
                                        <th>소계</th>
                                        <th>삭제</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <c:if test="${not empty item.product.imageUrl}">
                                                        <img src="${item.product.imageUrl}" 
                                                             alt="${item.product.sakeName}" 
                                                             style="width: 60px; height: 60px; object-fit: cover; margin-right: 10px;">
                                                    </c:if>
                                                    <div>
                                                        <strong>${item.product.sakeName}</strong><br>
                                                        <small class="text-muted">${item.product.brand} · ${item.product.region}</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="₩"/>
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/cart/update" class="d-inline">
                                                    <input type="hidden" name="cartItemId" value="${item.cartItem.cartItemId}">
                                                    <div class="input-group" style="width: 120px;">
                                                        <input type="number" class="form-control form-control-sm" 
                                                               name="quantity" value="${item.cartItem.quantity}" 
                                                               min="1" max="${item.product.stock}" required>
                                                        <button type="submit" class="btn btn-outline-secondary btn-sm">
                                                            <i class="fas fa-sync-alt"></i>
                                                        </button>
                                                    </div>
                                                </form>
                                            </td>
                                            <td>
                                                <strong>
                                                    <fmt:formatNumber value="${item.product.price * item.cartItem.quantity}" type="currency" currencySymbol="₩"/>
                                                </strong>
                                            </td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/cart/delete" 
                                                      onsubmit="return confirm('정말 삭제하시겠습니까?');" class="d-inline">
                                                    <input type="hidden" name="cartItemId" value="${item.cartItem.cartItemId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
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
                            <a href="${pageContext.request.contextPath}/order/form" class="btn btn-primary w-100 btn-lg">
                                <i class="fas fa-credit-card"></i> 주문하기
                            </a>
                            <a href="${pageContext.request.contextPath}/product/list" class="btn btn-outline-secondary w-100 mt-2">
                                <i class="fas fa-shopping-bag"></i> 쇼핑 계속하기
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

