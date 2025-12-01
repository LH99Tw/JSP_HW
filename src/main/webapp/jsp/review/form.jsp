<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 브레드크럼 -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">홈</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/sake/list">커뮤니티</a></li>
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/sake/detail?sakeId=${sake.sakeId}">${sake.displayName}</a>
            </li>
            <li class="breadcrumb-item active">리뷰 ${isEdit ? '수정' : '작성'}</li>
        </ol>
    </nav>
    
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4 class="mb-0">리뷰 ${isEdit ? '수정' : '작성'}</h4>
                </div>
                <div class="card-body">
                    <p class="text-muted mb-4">
                        <strong>사케:</strong> ${sake.displayName}
                        <c:if test="${not empty sake.brand}">
                            (${sake.brand})
                        </c:if>
                    </p>
                    
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <form id="reviewForm" method="post" 
                          action="${pageContext.request.contextPath}/review/${isEdit ? 'update' : 'create'}" 
                          enctype="multipart/form-data">
                        <input type="hidden" name="sakeId" value="${sake.sakeId}">
                        <c:if test="${isEdit}">
                            <input type="hidden" name="reviewId" value="${param.reviewId}">
                        </c:if>
                        
                        <!-- 별점 -->
                        <div class="mb-3">
                            <label class="form-label">별점 *</label>
                            <div class="rating-input">
                                <input type="hidden" name="rating" id="rating" value="5" required>
                                <div class="d-flex gap-2">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star fa-2x text-warning rating-star" 
                                           data-rating="${i}" 
                                           style="cursor: pointer; opacity: ${i <= 5 ? '1' : '0.3'};"></i>
                                    </c:forEach>
                                </div>
                                <small class="text-muted">별을 클릭하여 평점을 선택하세요</small>
                            </div>
                        </div>
                        
                        <!-- 리뷰 제목 -->
                        <div class="mb-3">
                            <label for="title" class="form-label">리뷰 제목</label>
                            <input type="text" class="form-control" id="title" name="title" 
                                   placeholder="리뷰 제목을 입력하세요" maxlength="100">
                        </div>
                        
                        <!-- 리뷰 내용 -->
                        <div class="mb-3">
                            <label for="content" class="form-label">리뷰 내용 *</label>
                            <textarea class="form-control" id="content" name="content" rows="6" 
                                      placeholder="리뷰 내용을 입력하세요 (최소 10자 이상)" 
                                      required minlength="10"></textarea>
                            <div class="form-text">최소 10자 이상 입력해주세요.</div>
                        </div>
                        
                        <!-- 맛 평가 -->
                        <div class="card mb-3">
                            <div class="card-header">
                                <h6 class="mb-0">맛 평가 (선택사항)</h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="sweetnessScore" class="form-label">단맛 (1~5)</label>
                                        <input type="range" class="form-range" id="sweetnessScore" name="sweetnessScore" 
                                               min="1" max="5" value="3" 
                                               oninput="document.getElementById('sweetnessValue').textContent = this.value">
                                        <div class="d-flex justify-content-between">
                                            <small>1 (카라구치)</small>
                                            <span id="sweetnessValue">3</span>
                                            <small>5 (아마구치)</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="drynessScore" class="form-label">드라이 (1~5)</label>
                                        <input type="range" class="form-range" id="drynessScore" name="drynessScore" 
                                               min="1" max="5" value="3"
                                               oninput="document.getElementById('drynessValue').textContent = this.value">
                                        <div class="d-flex justify-content-between">
                                            <small>1 (단맛)</small>
                                            <span id="drynessValue">3</span>
                                            <small>5 (드라이)</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="acidityScore" class="form-label">산미 (1~5)</label>
                                        <input type="range" class="form-range" id="acidityScore" name="acidityScore" 
                                               min="1" max="5" value="3"
                                               oninput="document.getElementById('acidityValue').textContent = this.value">
                                        <div class="d-flex justify-content-between">
                                            <small>1 (약함)</small>
                                            <span id="acidityValue">3</span>
                                            <small>5 (강함)</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="aromaScore" class="form-label">향기 (1~5)</label>
                                        <input type="range" class="form-range" id="aromaScore" name="aromaScore" 
                                               min="1" max="5" value="3"
                                               oninput="document.getElementById('aromaValue').textContent = this.value">
                                        <div class="d-flex justify-content-between">
                                            <small>1 (약함)</small>
                                            <span id="aromaValue">3</span>
                                            <small>5 (강함)</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="bodyScore" class="form-label">바디감 (1~5)</label>
                                        <input type="range" class="form-range" id="bodyScore" name="bodyScore" 
                                               min="1" max="5" value="3"
                                               oninput="document.getElementById('bodyValue').textContent = this.value">
                                        <div class="d-flex justify-content-between">
                                            <small>1 (라이트)</small>
                                            <span id="bodyValue">3</span>
                                            <small>5 (풀바디)</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- 사진 업로드 -->
                        <div class="mb-3">
                            <label for="photos" class="form-label">사진 첨부 (최대 5장)</label>
                            <input type="file" class="form-control" id="photos" name="photos" 
                                   accept="image/*" multiple>
                            <div class="form-text">JPG, PNG 형식만 가능합니다. (최대 5MB per file)</div>
                            <div id="photoPreview" class="mt-3 row"></div>
                        </div>
                        
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="${pageContext.request.contextPath}/sake/detail?sakeId=${sake.sakeId}" 
                               class="btn btn-secondary">취소</a>
                            <button type="submit" class="btn btn-primary">${isEdit ? '수정하기' : '작성하기'}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// 별점 선택
document.querySelectorAll('.rating-star').forEach(star => {
    star.addEventListener('click', function() {
        const rating = parseInt(this.getAttribute('data-rating'));
        document.getElementById('rating').value = rating;
        
        document.querySelectorAll('.rating-star').forEach((s, index) => {
            if (index < rating) {
                s.classList.remove('far');
                s.classList.add('fas');
                s.style.opacity = '1';
            } else {
                s.classList.remove('fas');
                s.classList.add('far');
                s.style.opacity = '0.3';
            }
        });
    });
    
    star.addEventListener('mouseenter', function() {
        const rating = parseInt(this.getAttribute('data-rating'));
        document.querySelectorAll('.rating-star').forEach((s, index) => {
            if (index < rating) {
                s.style.opacity = '1';
            } else {
                s.style.opacity = '0.3';
            }
        });
    });
});

// 사진 미리보기
document.getElementById('photos').addEventListener('change', function(e) {
    const preview = document.getElementById('photoPreview');
    preview.innerHTML = '';
    
    const files = e.target.files;
    if (files.length > 5) {
        alert('최대 5장까지만 업로드할 수 있습니다.');
        this.value = '';
        return;
    }
    
    Array.from(files).forEach((file, index) => {
        if (file.size > 5 * 1024 * 1024) {
            alert(file.name + ' 파일이 5MB를 초과합니다.');
            return;
        }
        
        const reader = new FileReader();
        reader.onload = function(e) {
            const col = document.createElement('div');
            col.className = 'col-md-3 mb-2';
            col.innerHTML = `
                <div class="position-relative">
                    <img src="${e.target.result}" class="img-thumbnail" style="width: 100%; height: 150px; object-fit: cover;">
                    <button type="button" class="btn btn-sm btn-danger position-absolute top-0 end-0 m-1" 
                            onclick="this.parentElement.parentElement.remove()">×</button>
                </div>
            `;
            preview.appendChild(col);
        };
        reader.readAsDataURL(file);
    });
});

// 폼 유효성 검사
document.getElementById('reviewForm').addEventListener('submit', function(e) {
    const content = document.getElementById('content').value.trim();
    if (content.length < 10) {
        e.preventDefault();
        alert('리뷰 내용은 최소 10자 이상 입력해주세요.');
        return false;
    }
    
    const rating = document.getElementById('rating').value;
    if (!rating || rating < 1 || rating > 5) {
        e.preventDefault();
        alert('별점을 선택해주세요.');
        return false;
    }
});
</script>

<%@ include file="/jsp/common/footer.jspf" %>

