package com.example.model;

import java.time.LocalDateTime;

/**
 * 게시글 추천 모델 클래스
 */
public class PostLike {
    private int likeId;
    private int postId;
    private int userId;
    private LocalDateTime createdAt;
    
    public PostLike() {}
    
    public PostLike(int likeId, int postId, int userId, LocalDateTime createdAt) {
        this.likeId = likeId;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getLikeId() { return likeId; }
    public void setLikeId(int likeId) { this.likeId = likeId; }
    
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

