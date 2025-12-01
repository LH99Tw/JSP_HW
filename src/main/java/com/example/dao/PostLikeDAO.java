package com.example.dao;

import com.example.model.PostLike;
import com.example.util.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 추천 DAO
 */
public class PostLikeDAO {
    
    /**
     * 사용자가 특정 게시글에 추천했는지 확인
     */
    public boolean exists(int postId, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM post_like WHERE post_id = ? AND user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return false;
    }
    
    /**
     * 추천 추가
     */
    public boolean insert(int postId, int userId) throws SQLException {
        String sql = "INSERT INTO post_like (post_id, user_id) VALUES (?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // UNIQUE 제약조건 위반 (이미 추천한 경우)
            if (e.getSQLState().equals("23505")) {
                return false;
            }
            throw e;
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 추천 삭제
     */
    public boolean delete(int postId, int userId) throws SQLException {
        String sql = "DELETE FROM post_like WHERE post_id = ? AND user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 게시글의 총 추천 수
     */
    public int getLikeCount(int postId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM post_like WHERE post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return 0;
    }
}

