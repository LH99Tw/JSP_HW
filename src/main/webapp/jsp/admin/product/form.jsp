<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4">
        <i class="fas fa-store"></i> 상품 ${isEdit ? '수정' : '등록'}
    </h2>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/admin/product/${isEdit ? 'update' : 'create'}" enctype="multipart/form-data">
        <c:if test="${isEdit}">
            <input type="hidden" name="productId" value="${product.productId}">
        </c:if>
        
        <div class="card">
            <div class="card-header">상품 정보</div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="sakeId" class="form-label">사케 <span class="text-danger">*</span></label>
                        <select class="form-select" id="sakeId" name="sakeId" required>
                            <option value="">선택하세요</option>
                            <c:forEach var="sake" items="${sakes}">
                                <option value="${sake.sakeId}" 
                                        ${isEdit && product.sakeId == sake.sakeId ? 'selected' : ''}>
                                    ${sake.nameKo} (${sake.brand})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="col-md-6 mb-3">
                        <label for="label" class="form-label">라벨</label>
                        <input type="text" class="form-control" id="label" name="label" 
                               value="${product.label}" placeholder="예: 프리미엄, 한정판, 신상">
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="price" class="form-label">가격 <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="price" name="price" 
                               value="${product.price}" min="0" required>
                    </div>
                    
                    <div class="col-md-4 mb-3">
                        <label for="stock" class="form-label">재고 <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="stock" name="stock" 
                               value="${product.stock}" min="0" required>
                    </div>
                    
                    <div class="col-md-4 mb-3">
                        <label class="form-label">노출 여부</label>
                        <div class="form-check form-switch mt-2">
                            <input class="form-check-input" type="checkbox" id="isPublished" name="isPublished" 
                                   ${isEdit && product.published ? 'checked' : 'checked'}>
                            <label class="form-check-label" for="isPublished">
                                상품 노출
                            </label>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-12 mb-3">
                        <label for="image" class="form-label">상품 이미지</label>
                        <input type="file" class="form-control" id="image" name="image" accept="image/*">
                        <small class="form-text text-muted">JPG, JPEG, PNG, GIF 파일만 업로드 가능 (최대 5MB)</small>
                        <c:if test="${isEdit && not empty product.imageUrl}">
                            <div class="mt-2">
                                <p class="mb-1">현재 이미지:</p>
                                <img src="${pageContext.request.contextPath}${product.imageUrl}" 
                                     alt="상품 이미지" style="max-height: 200px; max-width: 200px;" class="img-thumbnail">
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="d-flex justify-content-end gap-2 mt-4">
            <a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-secondary">
                <i class="fas fa-times"></i> 취소
            </a>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> ${isEdit ? '수정' : '등록'}
            </button>
        </div>
    </form>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

