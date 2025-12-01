package com.example.dao;

import com.example.model.SakeReview;
import com.example.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사케 리뷰 데이터 접근 객체
 */
public class ReviewDAO {
    
    /**
     * 사케별 리뷰 목록 조회
     */
    public List<SakeReview> findBySakeId(int sakeId) throws SQLException {
        String sql = "SELECT * FROM sake_review WHERE sake_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            rs = pstmt.executeQuery();
            
            List<SakeReview> reviewList = new ArrayList<>();
            while (rs.next()) {
                reviewList.add(mapResultSetToReview(rs));
            }
            return reviewList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사용자별 리뷰 목록 조회
     */
    public List<SakeReview> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM sake_review WHERE user_id = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            List<SakeReview> reviewList = new ArrayList<>();
            while (rs.next()) {
                reviewList.add(mapResultSetToReview(rs));
            }
            return reviewList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 리뷰 ID로 상세 조회
     */
    public SakeReview findById(int reviewId) throws SQLException {
        String sql = "SELECT * FROM sake_review WHERE review_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReview(rs);
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 리뷰 작성
     */
    public int insert(SakeReview review) throws SQLException {
        String sql = "INSERT INTO sake_review (sake_id, user_id, rating, title, content, " +
                     "sweetness_score, dryness_score, acidity_score, aroma_score, body_score, ml_tags) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getSakeId());
            pstmt.setInt(2, review.getUserId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getTitle());
            pstmt.setString(5, review.getContent());
            pstmt.setObject(6, review.getSweetnessScore());
            pstmt.setObject(7, review.getDrynessScore());
            pstmt.setObject(8, review.getAcidityScore());
            pstmt.setObject(9, review.getAromaScore());
            pstmt.setObject(10, review.getBodyScore());
            pstmt.setString(11, review.getMlTags());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 리뷰 수정
     */
    public boolean update(SakeReview review) throws SQLException {
        String sql = "UPDATE sake_review SET rating = ?, title = ?, content = ?, " +
                     "sweetness_score = ?, dryness_score = ?, acidity_score = ?, " +
                     "aroma_score = ?, body_score = ?, ml_tags = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE review_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, review.getRating());
            pstmt.setString(2, review.getTitle());
            pstmt.setString(3, review.getContent());
            pstmt.setObject(4, review.getSweetnessScore());
            pstmt.setObject(5, review.getDrynessScore());
            pstmt.setObject(6, review.getAcidityScore());
            pstmt.setObject(7, review.getAromaScore());
            pstmt.setObject(8, review.getBodyScore());
            pstmt.setString(9, review.getMlTags());
            pstmt.setInt(10, review.getReviewId());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 리뷰 삭제
     */
    public boolean delete(int reviewId) throws SQLException {
        String sql = "DELETE FROM sake_review WHERE review_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케의 평균 평점 계산
     */
    public Double getAverageRating(int sakeId) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM sake_review WHERE sake_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
            return 0.0;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케의 리뷰 수 계산
     */
    public int getReviewCount(int sakeId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM sake_review WHERE sake_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 리뷰 목록 조회
     */
    public List<SakeReview> findAll() throws SQLException {
        String sql = "SELECT * FROM sake_review ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<SakeReview> reviewList = new ArrayList<>();
            while (rs.next()) {
                reviewList.add(mapResultSetToReview(rs));
            }
            return reviewList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 리뷰 수 조회
     */
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sake_review";
        
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
     * ResultSet을 SakeReview 객체로 변환
     */
    private SakeReview mapResultSetToReview(ResultSet rs) throws SQLException {
        SakeReview review = new SakeReview();
        review.setReviewId(rs.getInt("review_id"));
        review.setSakeId(rs.getInt("sake_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setRating(rs.getInt("rating"));
        review.setTitle(rs.getString("title"));
        review.setContent(rs.getString("content"));
        review.setSweetnessScore(rs.getObject("sweetness_score") != null ? rs.getInt("sweetness_score") : null);
        review.setDrynessScore(rs.getObject("dryness_score") != null ? rs.getInt("dryness_score") : null);
        review.setAcidityScore(rs.getObject("acidity_score") != null ? rs.getInt("acidity_score") : null);
        review.setAromaScore(rs.getObject("aroma_score") != null ? rs.getInt("aroma_score") : null);
        review.setBodyScore(rs.getObject("body_score") != null ? rs.getInt("body_score") : null);
        review.setMlTags(rs.getString("ml_tags"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        review.setUpdatedAt(rs.getTimestamp("updated_at"));
        return review;
    }
}

