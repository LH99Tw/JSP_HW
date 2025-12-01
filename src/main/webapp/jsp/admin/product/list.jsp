<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-store"></i> 상품 관리</h2>
        <a href="${pageContext.request.contextPath}/admin/product/form" class="btn btn-primary">
            <i class="fas fa-plus"></i> 상품 등록
        </a>
    </div>
    
    <!-- 필터 영역 -->
    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/admin/product/list" class="row g-3">
                <div class="col-md-3">
                    <label for="region" class="form-label">지역</label>
                    <select class="form-select" id="region" name="region">
                        <option value="">전체</option>
                        <option value="후쿠시마" ${region == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                        <option value="아키타" ${region == '아키타' ? 'selected' : ''}>아키타</option>
                        <option value="야마가타" ${region == '야마가타' ? 'selected' : ''}>야마가타</option>
                        <option value="니가타" ${region == '니가타' ? 'selected' : ''}>니가타</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="style" class="form-label">스타일</label>
                    <select class="form-select" id="style" name="style">
                        <option value="">전체</option>
                        <optgroup label="정미율 기준 분류 - 준마이 계열">
                            <option value="준마이다이긴조" ${style == '준마이다이긴조' ? 'selected' : ''}>준마이다이긴조</option>
                            <option value="준마이긴조" ${style == '준마이긴조' ? 'selected' : ''}>준마이긴조</option>
                            <option value="준마이" ${style == '준마이' ? 'selected' : ''}>준마이</option>
                        </optgroup>
                        <optgroup label="정미율 기준 분류 - 혼조조 계열">
                            <option value="다이긴조" ${style == '다이긴조' ? 'selected' : ''}>다이긴조</option>
                            <option value="긴조" ${style == '긴조' ? 'selected' : ''}>긴조</option>
                            <option value="혼조조" ${style == '혼조조' ? 'selected' : ''}>혼조조</option>
                        </optgroup>
                        <optgroup label="제조 방식별 분류">
                            <option value="나마자케" ${style == '나마자케' ? 'selected' : ''}>나마자케</option>
                            <option value="나마초조" ${style == '나마초조' ? 'selected' : ''}>나마초조</option>
                            <option value="나마즈메" ${style == '나마즈메' ? 'selected' : ''}>나마즈메</option>
                            <option value="니고리자케" ${style == '니고리자케' ? 'selected' : ''}>니고리자케</option>
                            <option value="키죠슈" ${style == '키죠슈' ? 'selected' : ''}>키죠슈</option>
                        </optgroup>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="published" class="form-label">노출 여부</label>
                    <select class="form-select" id="published" name="published">
                        <option value="">전체</option>
                        <option value="true" ${published == 'true' ? 'selected' : ''}>노출</option>
                        <option value="false" ${published == 'false' ? 'selected' : ''}>비노출</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="search" class="form-label">검색</label>
                    <input type="text" class="form-control" id="search" name="search" 
                           value="${search}" placeholder="상품명, 브랜드 검색">
                </div>
                <div class="col-md-12">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> 검색
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-outline-secondary">
                        <i class="fas fa-redo"></i> 초기화
                    </a>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 상품 목록 -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>상품명</th>
                            <th>브랜드</th>
                            <th>지역</th>
                            <th>가격</th>
                            <th>재고</th>
                            <th>노출</th>
                            <th>관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty products}">
                                <tr>
                                    <td colspan="8" class="text-center py-4">등록된 상품이 없습니다.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td>${product.productId}</td>
                                        <td>${product.sakeName}</td>
                                        <td>${product.brand}</td>
                                        <td>${product.region}</td>
                                        <td>
                                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₩"/>
                                        </td>
                                        <td>
                                            <span class="${product.stock < 10 ? 'text-danger fw-bold' : ''}">
                                                ${product.stock}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.published}">
                                                    <span class="badge bg-success">노출</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">비노출</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/product/form?productId=${product.productId}" 
                                               class="btn btn-sm btn-outline-primary">
                                                <i class="fas fa-edit"></i> 수정
                                            </a>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/product/delete" 
                                                  style="display: inline;" 
                                                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                                <input type="hidden" name="productId" value="${product.productId}">
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

