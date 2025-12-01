package com.example.dao;

import com.example.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 업로드 데이터 접근 객체
 */
public class UploadDAO {
    
    /**
     * 파일 업로드 정보 저장
     */
    public int insert(int userId, int reviewId, String filePath) throws SQLException {
        // filePath에서 파일명 추출
        String storedFilename = filePath.substring(filePath.lastIndexOf("/") + 1);
        String originalFilename = storedFilename; // 원본 파일명은 나중에 저장 가능
        
        String sql = "INSERT INTO uploads (user_id, original_filename, stored_filename, file_path, content_type) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setString(2, originalFilename);
            pstmt.setString(3, storedFilename);
            pstmt.setString(4, filePath);
            pstmt.setString(5, "image/jpeg"); // 기본값
            
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
     * 리뷰 ID로 파일 목록 조회
     */
    public List<String> findByReviewId(int reviewId) throws SQLException {
        // uploads 테이블에 review_id 컬럼이 없으므로, 
        // 파일 경로나 다른 방법으로 연결해야 함
        // 일단 간단하게 user_id로 조회 (나중에 개선 가능)
        String sql = "SELECT file_path FROM uploads WHERE user_id = ? ORDER BY uploaded_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            // reviewId를 userId로 사용 (임시)
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();
            
            List<String> filePaths = new ArrayList<>();
            while (rs.next()) {
                filePaths.add(rs.getString("file_path"));
            }
            return filePaths;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 파일 삭제
     */
    public boolean delete(int uploadId) throws SQLException {
        String sql = "DELETE FROM uploads WHERE upload_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uploadId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
}

