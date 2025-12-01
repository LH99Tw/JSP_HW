package com.example.dao;

import com.example.model.User;
import com.example.util.DataSource;
import java.sql.*;

/**
 * 사용자 데이터베이스 접근 객체
 */
public class UserDAO {
    
    /**
     * 사용자명으로 사용자 조회
     */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, email, password, full_name, language, created_at, updated_at " +
                     "FROM users WHERE username = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 이메일로 사용자 조회
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT user_id, username, email, password, full_name, language, created_at, updated_at " +
                     "FROM users WHERE email = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 사용자 ID로 조회
     */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT user_id, username, email, password, full_name, language, created_at, updated_at " +
                     "FROM users WHERE user_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 새 사용자 생성
     */
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, full_name, language) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING user_id";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword()); // 실제로는 해싱 필요
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getLanguage() != null ? user.getLanguage() : "ko");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        return -1;
    }
    
    /**
     * 사용자 정보 업데이트
     */
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, language = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE user_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getLanguage());
            pstmt.setInt(3, user.getUserId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * 비밀번호 변경
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword); // 실제로는 해싱 필요
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * 사용자명 중복 체크
     */
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * 이메일 중복 체크
     */
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * ResultSet을 User 객체로 변환
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setLanguage(rs.getString("language"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}

