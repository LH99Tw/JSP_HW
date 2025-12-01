<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <div class="card">
                <div class="card-header">
                    <h4 class="mb-0">게시글 ${post != null ? '수정' : '작성'}</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </c:if>
                    
                    <form method="post" action="${pageContext.request.contextPath}/board/${post != null ? 'update' : 'write'}">
                        <c:if test="${post != null}">
                            <input type="hidden" name="postId" value="${post.postId}">
                        </c:if>
                        
                        <div class="mb-3">
                            <label for="category" class="form-label">카테고리</label>
                            <select class="form-select" id="category" name="category" required>
                                <option value="일반" ${post != null && post.category == '일반' ? 'selected' : ''}>일반</option>
                                <option value="질문" ${post != null && post.category == '질문' ? 'selected' : ''}>질문</option>
                                <option value="리뷰(술)" ${post != null && post.category == '리뷰(술)' ? 'selected' : ''}>리뷰(술)</option>
                                <option value="주점" ${post != null && post.category == '주점' ? 'selected' : ''}>주점</option>
                                <option value="주판점" ${post != null && post.category == '주판점' ? 'selected' : ''}>주판점</option>
                                <option value="양조장" ${post != null && post.category == '양조장' ? 'selected' : ''}>양조장</option>
                                <option value="여행후기" ${post != null && post.category == '여행후기' ? 'selected' : ''}>여행후기</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="prefix" class="form-label">말머리 (선택)</label>
                            <input type="text" class="form-control" id="prefix" name="prefix" 
                                   value="${post != null ? post.prefix : ''}" 
                                   placeholder="예: 설문, AD 등">
                        </div>
                        
                        <div class="mb-3">
                            <label for="title" class="form-label">제목</label>
                            <input type="text" class="form-control" id="title" name="title" 
                                   value="${post != null ? post.title : ''}" 
                                   maxlength="200" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="content" class="form-label">내용</label>
                            <textarea class="form-control" id="content" name="content" rows="15" required>${post != null ? post.content : ''}</textarea>
                        </div>
                        
                        <div class="d-flex justify-content-end">
                            <a href="${pageContext.request.contextPath}/board/list" class="btn btn-secondary me-2">취소</a>
                            <button type="submit" class="btn btn-primary">${post != null ? '수정' : '작성'}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

