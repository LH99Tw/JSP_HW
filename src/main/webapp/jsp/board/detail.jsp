<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 게시글 상세 -->
    <div class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
            <div>
                <c:if test="${post.notice}">
                    <span class="badge bg-danger me-2">공지</span>
                </c:if>
                <c:if test="${not empty post.prefix}">
                    <span class="badge bg-secondary me-2">${post.prefix}</span>
                </c:if>
                <span class="badge bg-info">${post.category}</span>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/board/list" class="btn btn-sm btn-outline-secondary">
                    목록
                </a>
            </div>
        </div>
        <div class="card-body">
            <h3 class="card-title mb-3">${post.title}</h3>
            <div class="d-flex justify-content-between text-muted mb-3">
                <div>
                    <span>글쓴이: ${post.username}</span>
                    <span class="ms-3">
                        <%
                            if (request.getAttribute("post") != null) {
                                com.example.model.Post post = (com.example.model.Post) request.getAttribute("post");
                                if (post.getCreatedAt() != null) {
                                    java.time.LocalDateTime ldt = post.getCreatedAt();
                                    java.util.Date date = java.sql.Timestamp.valueOf(ldt);
                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm");
                                    out.print(sdf.format(date));
                                }
                            }
                        %>
                    </span>
                </div>
                <div>
                    <span>조회 ${post.viewCount}</span>
                    <span class="ms-2">추천 ${post.likeCount}</span>
                </div>
            </div>
            <hr>
            <div class="card-text" style="min-height: 200px;">
                ${post.content}
            </div>
        </div>
        <div class="card-footer d-flex justify-content-between">
            <div>
                <button type="button" class="btn btn-outline-primary" id="likeBtn" 
                        data-post-id="${post.postId}" data-liked="${isLiked}">
                    <i class="fas fa-thumbs-up"></i> 추천 <span id="likeCount">${post.likeCount}</span>
                </button>
            </div>
            <div>
                <c:if test="${sessionScope.user.userId == post.userId || sessionScope.user.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/board/update?postId=${post.postId}" 
                       class="btn btn-outline-secondary btn-sm">수정</a>
                    <form method="post" action="${pageContext.request.contextPath}/board/delete" 
                          style="display: inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                        <input type="hidden" name="postId" value="${post.postId}">
                        <button type="submit" class="btn btn-outline-danger btn-sm">삭제</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
    
    <!-- 댓글 영역 -->
    <div class="card">
        <div class="card-header">
            <h5 class="mb-0">댓글 <span class="badge bg-primary">${post.commentCount}</span></h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <p class="text-muted">댓글을 작성하려면 <a href="${pageContext.request.contextPath}/login">로그인</a>이 필요합니다.</p>
                </c:when>
                <c:otherwise>
                    <!-- 댓글 작성 폼 -->
                    <form method="post" action="${pageContext.request.contextPath}/comment/create" class="mb-4">
                        <input type="hidden" name="postId" value="${post.postId}">
                        <div class="mb-3">
                            <textarea class="form-control" name="content" rows="3" 
                                      placeholder="댓글을 입력하세요..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">댓글 작성</button>
                    </form>
                </c:otherwise>
            </c:choose>
            
            <!-- 댓글 목록 -->
            <div class="comment-list">
                <c:choose>
                    <c:when test="${empty comments}">
                        <p class="text-muted text-center py-3">댓글이 없습니다.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="comment" items="${comments}">
                            <div class="border-bottom pb-3 mb-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <div>
                                        <strong>${comment.username}</strong>
                                        <span class="text-muted ms-2">
                                            <%
                                                com.example.model.PostComment currentComment = (com.example.model.PostComment) pageContext.getAttribute("comment");
                                                if (currentComment != null && currentComment.getCreatedAt() != null) {
                                                    java.time.LocalDateTime ldt = currentComment.getCreatedAt();
                                                    java.util.Date date = java.sql.Timestamp.valueOf(ldt);
                                                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm");
                                                    out.print(sdf.format(date));
                                                }
                                            %>
                                        </span>
                                    </div>
                                    <c:if test="${sessionScope.user.userId == comment.userId || sessionScope.user.role == 'ADMIN'}">
                                        <div>
                                            <button class="btn btn-sm btn-outline-secondary" 
                                                    onclick="deleteComment(${comment.commentId}, ${post.postId})">삭제</button>
                                        </div>
                                    </c:if>
                                </div>
                                <div>${comment.content}</div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

<script>
    // 추천 버튼 클릭
    document.getElementById('likeBtn').addEventListener('click', function() {
        const postId = this.getAttribute('data-post-id');
        const isLiked = this.getAttribute('data-liked') === 'true';
        
        fetch('${pageContext.request.contextPath}/board/like', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'postId=' + postId
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const likeCountEl = document.getElementById('likeCount');
                const currentCount = parseInt(likeCountEl.textContent);
                
                if (data.liked) {
                    likeCountEl.textContent = currentCount + 1;
                    this.setAttribute('data-liked', 'true');
                    this.classList.add('btn-primary');
                    this.classList.remove('btn-outline-primary');
                } else {
                    likeCountEl.textContent = Math.max(0, currentCount - 1);
                    this.setAttribute('data-liked', 'false');
                    this.classList.remove('btn-primary');
                    this.classList.add('btn-outline-primary');
                }
            } else {
                alert(data.message || '오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
    });
    
    // 댓글 삭제
    function deleteComment(commentId, postId) {
        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return;
        }
        
        fetch('${pageContext.request.contextPath}/comment/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'commentId=' + commentId + '&postId=' + postId
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('댓글 삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
    }
</script>

