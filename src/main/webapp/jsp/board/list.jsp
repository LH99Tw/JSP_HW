<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/jsp/common/header.jspf" %>

<div class="container mt-4">
    <!-- 탭 메뉴 -->
    <ul class="nav nav-tabs mb-3" id="categoryTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '전체글' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=전체글&sortBy=${sortBy}">
                전체글
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '인기글' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=전체글&sortBy=인기글">
                인기글
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '공지' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=공지&sortBy=${sortBy}">
                공지
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '일반' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=일반&sortBy=${sortBy}">
                일반
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '리뷰(술)' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=리뷰(술)&sortBy=${sortBy}">
                리뷰(술)
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '질문' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=질문&sortBy=${sortBy}">
                질문
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '주점' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=주점&sortBy=${sortBy}">
                주점
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '주판점' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=주판점&sortBy=${sortBy}">
                주판점
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '양조장' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=양조장&sortBy=${sortBy}">
                양조장
            </a>
        </li>
        <li class="nav-item" role="presentation">
            <a class="nav-link ${category == '여행후기' ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/board/list?category=여행후기&sortBy=${sortBy}">
                여행후기
            </a>
        </li>
    </ul>
    
    <!-- 상단 옵션 바 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="d-flex align-items-center">
            <select class="form-select form-select-sm me-2" style="width: auto;" 
                    onchange="location.href='${pageContext.request.contextPath}/board/list?category=${category}&sortBy=' + this.value">
                <option value="전체글" ${sortBy == '전체글' ? 'selected' : ''}>최신순</option>
                <option value="인기글" ${sortBy == '인기글' ? 'selected' : ''}>인기글</option>
                <option value="조회순" ${sortBy == '조회순' ? 'selected' : ''}>조회순</option>
            </select>
            <select class="form-select form-select-sm" style="width: auto;">
                <option>50개</option>
                <option>30개</option>
                <option>20개</option>
            </select>
        </div>
        <a href="${pageContext.request.contextPath}/board/write" class="btn btn-primary">
            <i class="fas fa-pen"></i> 글쓰기
        </a>
    </div>
    
    <!-- 게시글 테이블 -->
    <table class="table table-hover">
        <thead class="table-light">
            <tr>
                <th style="width: 60px;">번호</th>
                <th style="width: 80px;">말머리</th>
                <th>제목</th>
                <th style="width: 100px;">글쓴이</th>
                <th style="width: 120px;">작성일</th>
                <th style="width: 70px;">조회</th>
                <th style="width: 70px;">추천</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty posts}">
                    <tr>
                        <td colspan="7" class="text-center py-4">게시글이 없습니다.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="post" items="${posts}">
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${post.notice}">
                                        <span class="badge bg-danger">공지</span>
                                    </c:when>
                                    <c:otherwise>
                                        ${post.postId}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty post.prefix}">
                                    <span class="badge bg-secondary">${post.prefix}</span>
                                </c:if>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/board/detail?postId=${post.postId}" 
                                   class="text-decoration-none text-dark">
                                    ${post.title}
                                    <c:if test="${post.commentCount > 0}">
                                        <span class="text-primary">[${post.commentCount}]</span>
                                    </c:if>
                                </a>
                            </td>
                            <td>${post.username}</td>
                            <td>
                                <%
                                    // LocalDateTime을 Date로 변환
                                    com.example.model.Post currentPost = (com.example.model.Post) pageContext.getAttribute("post");
                                    if (currentPost != null && currentPost.getCreatedAt() != null) {
                                        java.time.LocalDateTime ldt = currentPost.getCreatedAt();
                                        java.util.Date date = java.sql.Timestamp.valueOf(ldt);
                                        
                                        // 오늘 날짜와 비교
                                        java.util.Date now = new java.util.Date();
                                        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yy.MM.dd");
                                        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");
                                        
                                        String postDateStr = dateFormat.format(date);
                                        String todayStr = dateFormat.format(now);
                                        
                                        if (postDateStr.equals(todayStr)) {
                                            out.print(timeFormat.format(date));
                                        } else {
                                            out.print(postDateStr);
                                        }
                                    }
                                %>
                            </td>
                            <td>${post.viewCount}</td>
                            <td>${post.likeCount}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    
    <!-- 페이징 -->
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <c:if test="${page > 1}">
                <li class="page-item">
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/board/list?category=${category}&sortBy=${sortBy}&page=${page - 1}">
                        이전
                    </a>
                </li>
            </c:if>
            
            <c:forEach var="i" begin="${page > 5 ? page - 4 : 1}" 
                       end="${page + 4 < totalPages ? page + 4 : totalPages}">
                <li class="page-item ${i == page ? 'active' : ''}">
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/board/list?category=${category}&sortBy=${sortBy}&page=${i}">
                        ${i}
                    </a>
                </li>
            </c:forEach>
            
            <c:if test="${page < totalPages}">
                <li class="page-item">
                    <a class="page-link" 
                       href="${pageContext.request.contextPath}/board/list?category=${category}&sortBy=${sortBy}&page=${page + 1}">
                        다음
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</div>

<%@ include file="/jsp/common/footer.jspf" %>

