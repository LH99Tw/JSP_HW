package com.example.model;

import java.sql.Timestamp;

/**
 * 장바구니 아이템 모델 클래스
 */
public class CartItem {
    private int cartItemId;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp createdAt;
    
    // 조인을 위한 추가 필드 (선택사항)
    private SakeProduct product;
    private Sake sake;
    
    // 기본 생성자
    public CartItem() {
    }
    
    // Getters and Setters
    public int getCartItemId() {
        return cartItemId;
    }
    
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public SakeProduct getProduct() {
        return product;
    }
    
    public void setProduct(SakeProduct product) {
        this.product = product;
    }
    
    public Sake getSake() {
        return sake;
    }
    
    public void setSake(Sake sake) {
        this.sake = sake;
    }
    
    // 편의 메서드
    public int getTotalPrice() {
        if (product != null) {
            return product.getPrice() * quantity;
        }
        return 0;
    }
}

