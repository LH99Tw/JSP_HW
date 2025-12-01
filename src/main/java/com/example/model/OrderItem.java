package com.example.model;

/**
 * 주문 상품 모델 클래스
 */
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private int price; // 주문 시점의 가격
    
    // 조인을 위한 추가 필드 (선택사항)
    private SakeProduct product;
    private Sake sake;
    
    // 기본 생성자
    public OrderItem() {
    }
    
    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }
    
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
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
        return price * quantity;
    }
}

