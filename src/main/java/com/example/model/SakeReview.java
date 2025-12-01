package com.example.model;

import java.sql.Timestamp;

/**
 * 사케 리뷰 모델 클래스
 */
public class SakeReview {
    private int reviewId;
    private int sakeId;
    private int userId;
    private int rating; // 1~5
    private String title;
    private String content;
    private Integer sweetnessScore; // 1~5
    private Integer drynessScore; // 1~5
    private Integer acidityScore; // 1~5
    private Integer aromaScore; // 1~5
    private Integer bodyScore; // 1~5
    private String mlTags; // JSON 형식
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // 기본 생성자
    public SakeReview() {
    }
    
    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public int getSakeId() {
        return sakeId;
    }
    
    public void setSakeId(int sakeId) {
        this.sakeId = sakeId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getSweetnessScore() {
        return sweetnessScore;
    }
    
    public void setSweetnessScore(Integer sweetnessScore) {
        this.sweetnessScore = sweetnessScore;
    }
    
    public Integer getDrynessScore() {
        return drynessScore;
    }
    
    public void setDrynessScore(Integer drynessScore) {
        this.drynessScore = drynessScore;
    }
    
    public Integer getAcidityScore() {
        return acidityScore;
    }
    
    public void setAcidityScore(Integer acidityScore) {
        this.acidityScore = acidityScore;
    }
    
    public Integer getAromaScore() {
        return aromaScore;
    }
    
    public void setAromaScore(Integer aromaScore) {
        this.aromaScore = aromaScore;
    }
    
    public Integer getBodyScore() {
        return bodyScore;
    }
    
    public void setBodyScore(Integer bodyScore) {
        this.bodyScore = bodyScore;
    }
    
    public String getMlTags() {
        return mlTags;
    }
    
    public void setMlTags(String mlTags) {
        this.mlTags = mlTags;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

