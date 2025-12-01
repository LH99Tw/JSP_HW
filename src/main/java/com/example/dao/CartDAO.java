package com.example.dao;

import com.example.model.CartItem;
import com.example.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 데이터 접근 객체
 */
public class CartDAO {
    
    /**
     * 사용자 장바구니 목록 조회
     */
    public List<CartItem> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM cart_item WHERE user_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            List<CartItem> cartList = new ArrayList<>();
            while (rs.next()) {
                cartList.add(mapResultSetToCartItem(rs));
            }
            return cartList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 장바구니에 상품 추가 (이미 있으면 수량 증가)
     */
    public boolean insert(CartItem item) throws SQLException {
        // 먼저 기존 아이템이 있는지 확인
        String checkSql = "SELECT cart_item_id, quantity FROM cart_item WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart_item SET quantity = quantity + ? WHERE cart_item_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, item.getUserId());
            pstmt.setInt(2, item.getProductId());
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // 기존 아이템이 있으면 수량 증가
                int cartItemId = rs.getInt("cart_item_id");
                pstmt.close();
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, item.getQuantity());
                pstmt.setInt(2, cartItemId);
                return pstmt.executeUpdate() > 0;
            } else {
                // 새로 추가
                pstmt.close();
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setInt(1, item.getUserId());
                pstmt.setInt(2, item.getProductId());
                pstmt.setInt(3, item.getQuantity());
                return pstmt.executeUpdate() > 0;
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 장바구니 수량 변경
     */
    public boolean updateQuantity(int cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart_item SET quantity = ? WHERE cart_item_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, cartItemId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 장바구니 아이템 삭제
     */
    public boolean delete(int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_item WHERE cart_item_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cartItemId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사용자 장바구니 비우기
     */
    public boolean clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart_item WHERE user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            return pstmt.executeUpdate() >= 0; // 0개 삭제도 성공으로 간주
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * ResultSet을 CartItem 객체로 변환
     */
    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        return item;
    }
}

