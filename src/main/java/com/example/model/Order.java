package com.example.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 모델 클래스
 */
public class Order {
    private int orderId;
    private int userId;
    private int totalAmount;
    private String status; // REQUESTED, PROCESSING, COMPLETED, CANCELLED
    private String receiverName;
    private String address;
    private String phone;
    private Timestamp createdAt;
    
    // 추가 배송 정보 필드
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
    private String shippingAddressDetail;
    private String shippingPostcode;
    private String memo;
    
    // 주문 상품 목록
    private List<OrderItem> items;
    
    // 기본 생성자
    public Order() {
        this.items = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    public void addItem(OrderItem item) {
        this.items.add(item);
    }
    
    // 추가 필드 Getters and Setters
    public String getRecipientName() {
        return recipientName != null ? recipientName : receiverName;
    }
    
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        this.receiverName = recipientName; // 호환성 유지
    }
    
    public String getRecipientPhone() {
        return recipientPhone != null ? recipientPhone : phone;
    }
    
    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
        this.phone = recipientPhone; // 호환성 유지
    }
    
    public String getShippingAddress() {
        return shippingAddress != null ? shippingAddress : address;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        this.address = shippingAddress; // 호환성 유지
    }
    
    public String getShippingAddressDetail() {
        return shippingAddressDetail;
    }
    
    public void setShippingAddressDetail(String shippingAddressDetail) {
        this.shippingAddressDetail = shippingAddressDetail;
    }
    
    public String getShippingPostcode() {
        return shippingPostcode;
    }
    
    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }
    
    public String getMemo() {
        return memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
}

