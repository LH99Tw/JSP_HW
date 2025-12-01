package com.example.dao;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 데이터 접근 객체
 */
public class OrderDAO {
    
    /**
     * 주문 생성
     */
    public int insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, receiver_name, address, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getTotalAmount());
            pstmt.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
            pstmt.setString(4, order.getRecipientName());
            pstmt.setString(5, order.getShippingAddress());
            pstmt.setString(6, order.getRecipientPhone());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    conn.commit();
                    return orderId;
                }
            }
            conn.rollback();
            return -1;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                DataSource.closeConnection(conn);
            }
        }
    }
    
    /**
     * 주문 상품 추가
     */
    public boolean insertOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) " +
                     "VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, item.getOrderId());
            pstmt.setInt(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setInt(4, item.getPrice());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사용자 주문 목록 조회
     */
    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            List<Order> orderList = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // 주문 상품 목록도 함께 조회
                order.setItems(findOrderItemsByOrderId(order.getOrderId()));
                orderList.add(order);
            }
            return orderList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 주문 ID로 상세 조회
     */
    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // 주문 상품 목록도 함께 조회
                order.setItems(findOrderItemsByOrderId(orderId));
                return order;
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 주문 상품 목록 조회
     */
    private List<OrderItem> findOrderItemsByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            
            List<OrderItem> itemList = new ArrayList<>();
            while (rs.next()) {
                itemList.add(mapResultSetToOrderItem(rs));
            }
            return itemList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * ResultSet을 Order 객체로 변환
     */
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getInt("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setReceiverName(rs.getString("receiver_name"));
        order.setAddress(rs.getString("address"));
        order.setPhone(rs.getString("phone"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }
    
    /**
     * 전체 주문 목록 조회 (관리자용)
     */
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<Order> orderList = new ArrayList<>();
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // 주문 상품 목록도 함께 조회
                order.setItems(findOrderItemsByOrderId(order.getOrderId()));
                orderList.add(order);
            }
            return orderList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 오늘 주문 수 조회
     */
    public int getTodayOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(created_at) = CURRENT_DATE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * ResultSet을 OrderItem 객체로 변환
     */
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getInt("price"));
        return item;
    }
}

