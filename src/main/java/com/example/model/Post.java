package com.example.model;

import java.time.LocalDateTime;

/**
 * 게시글 모델 클래스
 */
public class Post {
    private int postId;
    private int userId;
    private String category; // 공지, 일반, 질문, 리뷰(술), 주점, 주판점, 양조장, 여행후기
    private String prefix; // 말머리
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private boolean isNotice;
    private boolean isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 조인용 필드
    private String username;
    private String fullName;
    
    public Post() {}
    
    public Post(int postId, int userId, String category, String prefix, String title, 
                String content, int viewCount, int likeCount, int commentCount,
                boolean isNotice, boolean isPinned, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.category = category;
        this.prefix = prefix;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isNotice = isNotice;
        this.isPinned = isPinned;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    
    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    
    public boolean isNotice() { return isNotice; }
    public void setNotice(boolean notice) { isNotice = notice; }
    
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}

