package com.example.model;

import java.sql.Timestamp;

/**
 * 사케 상품 모델 클래스
 */
public class SakeProduct {
    private int productId;
    private int sakeId;
    private int price;
    private int stock;
    private boolean isPublished;
    private String label; // 한정판, 프리미엄 등
    private Timestamp createdAt;
    
    // 사케 정보 (JOIN으로 가져옴)
    private String sakeName;
    private String brand;
    private String region;
    private String style;
    private String imageUrl;
    
    // 기본 생성자
    public SakeProduct() {
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getSakeId() {
        return sakeId;
    }
    
    public void setSakeId(int sakeId) {
        this.sakeId = sakeId;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public boolean isPublished() {
        return isPublished;
    }
    
    public void setPublished(boolean published) {
        isPublished = published;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isInStock() {
        return stock > 0;
    }
    
    // 사케 정보 Getters and Setters
    public String getSakeName() {
        return sakeName;
    }
    
    public void setSakeName(String sakeName) {
        this.sakeName = sakeName;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

