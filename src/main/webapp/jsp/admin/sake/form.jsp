<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>
<%@ include file="/jsp/admin/common/header.jspf" %>

<div class="container mt-4">
    <h2 class="mb-4">
        <i class="fas fa-wine-bottle"></i> 사케 ${isEdit ? '수정' : '등록'}
    </h2>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/admin/sake/${isEdit ? 'update' : 'create'}" 
          enctype="multipart/form-data">
        <c:if test="${isEdit}">
            <input type="hidden" name="sakeId" value="${sake.sakeId}">
        </c:if>
        
        <div class="row">
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-header">기본 정보</div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="nameJa" class="form-label">사케명 (일본어) <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="nameJa" name="nameJa" 
                                   value="${sake.nameJa}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="nameEn" class="form-label">사케명 (영어)</label>
                            <input type="text" class="form-control" id="nameEn" name="nameEn" 
                                   value="${sake.nameEn}">
                        </div>
                        
                        <div class="mb-3">
                            <label for="nameKo" class="form-label">사케명 (한국어) <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="nameKo" name="nameKo" 
                                   value="${sake.nameKo}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="brand" class="form-label">브랜드</label>
                            <input type="text" class="form-control" id="brand" name="brand" 
                                   value="${sake.brand}">
                        </div>
                        
                        <div class="mb-3">
                            <label for="brewery" class="form-label">주조장</label>
                            <input type="text" class="form-control" id="brewery" name="brewery" 
                                   value="${sake.brewery}">
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-header">상세 정보</div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="region" class="form-label">지역</label>
                            <select class="form-select" id="region" name="region">
                                <option value="">선택</option>
                                <option value="홋카이도" ${sake.regionPrefecture == '홋카이도' ? 'selected' : ''}>홋카이도</option>
                                <option value="니가타" ${sake.regionPrefecture == '니가타' ? 'selected' : ''}>니가타</option>
                                <option value="야마가타" ${sake.regionPrefecture == '야마가타' ? 'selected' : ''}>야마가타</option>
                                <option value="후쿠시마" ${sake.regionPrefecture == '후쿠시마' ? 'selected' : ''}>후쿠시마</option>
                                <option value="아키타" ${sake.regionPrefecture == '아키타' ? 'selected' : ''}>아키타</option>
                                <option value="야마구치" ${sake.regionPrefecture == '야마구치' ? 'selected' : ''}>야마구치</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="style" class="form-label">스타일</label>
                            <select class="form-select" id="style" name="style">
                                <option value="">선택</option>
                                <optgroup label="준마이 계열">
                                    <option value="준마이다이긴조" ${sake.style == '준마이다이긴조' ? 'selected' : ''}>준마이다이긴조</option>
                                    <option value="준마이긴조" ${sake.style == '준마이긴조' ? 'selected' : ''}>준마이긴조</option>
                                    <option value="준마이" ${sake.style == '준마이' ? 'selected' : ''}>준마이</option>
                                </optgroup>
                                <optgroup label="혼조조 계열">
                                    <option value="다이긴조" ${sake.style == '다이긴조' ? 'selected' : ''}>다이긴조</option>
                                    <option value="긴조" ${sake.style == '긴조' ? 'selected' : ''}>긴조</option>
                                    <option value="혼조조" ${sake.style == '혼조조' ? 'selected' : ''}>혼조조</option>
                                </optgroup>
                            </select>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="alcoholPercent" class="form-label">도수 (%)</label>
                                <input type="number" step="0.1" class="form-control" id="alcoholPercent" 
                                       name="alcoholPercent" value="${sake.alcoholPercent}">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="volumeMl" class="form-label">용량 (ml)</label>
                                <input type="number" class="form-control" id="volumeMl" name="volumeMl" 
                                       value="${sake.volumeMl}">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="polishingRatio" class="form-label">정미율 (%)</label>
                                <input type="number" class="form-control" id="polishingRatio" 
                                       name="polishingRatio" value="${sake.polishingRatio}">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="nihonshuDo" class="form-label">일본주도</label>
                                <input type="number" step="0.1" class="form-control" id="nihonshuDo" 
                                       name="nihonshuDo" value="${sake.nihonshuDo}">
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="acidity" class="form-label">산도</label>
                            <input type="number" step="0.01" class="form-control" id="acidity" 
                                   name="acidity" value="${sake.acidity}">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-header">맛 프로파일</div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="sweetnessLevel" class="form-label">단맛/드라이 (1=카라구치, 5=아마구치)</label>
                            <input type="number" min="1" max="5" class="form-control" id="sweetnessLevel" 
                                   name="sweetnessLevel" value="${sake.sweetnessLevel}">
                        </div>
                        
                        <div class="mb-3">
                            <label for="aromaType" class="form-label">향 타입</label>
                            <select class="form-select" id="aromaType" name="aromaType">
                                <option value="">선택</option>
                                <option value="fruity" ${sake.aromaType == 'fruity' ? 'selected' : ''}>과일향</option>
                                <option value="floral" ${sake.aromaType == 'floral' ? 'selected' : ''}>꽃향</option>
                                <option value="rice" ${sake.aromaType == 'rice' ? 'selected' : ''}>쌀향</option>
                                <option value="earthy" ${sake.aromaType == 'earthy' ? 'selected' : ''}>흙향</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="bodyLevel" class="form-label">바디감 (1=라이트, 5=풀바디)</label>
                            <input type="number" min="1" max="5" class="form-control" id="bodyLevel" 
                                   name="bodyLevel" value="${sake.bodyLevel}">
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card mb-4">
                    <div class="card-header">이미지</div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label for="thumbnailPath" class="form-label">대표 이미지 경로</label>
                            <input type="text" class="form-control" id="thumbnailPath" name="thumbnailPath" 
                                   value="${sake.thumbnailPath}" placeholder="/images/sake/example.jpg">
                            <small class="form-text text-muted">파일 업로드 기능은 추후 구현 예정</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="d-flex justify-content-end gap-2">
            <a href="${pageContext.request.contextPath}/admin/sake/list" class="btn btn-secondary">
                <i class="fas fa-times"></i> 취소
            </a>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> ${isEdit ? '수정' : '등록'}
            </button>
        </div>
    </form>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

