package com.example.dao;

import com.example.model.FileInfo;
import com.example.util.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일 데이터베이스 접근 객체
 */
public class FileDAO {
    
    /**
     * 파일 정보 저장
     */
    public int insert(FileInfo fileInfo) throws SQLException {
        String sql = "INSERT INTO uploads (user_id, original_filename, stored_filename, file_path, file_size, content_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING upload_id";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, fileInfo.getUserId());
            pstmt.setString(2, fileInfo.getOriginalFilename());
            pstmt.setString(3, fileInfo.getStoredFilename());
            pstmt.setString(4, fileInfo.getFilePath());
            pstmt.setLong(5, fileInfo.getFileSize());
            pstmt.setString(6, fileInfo.getContentType());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("upload_id");
                }
            }
        }
        return -1;
    }
    
    /**
     * 파일 ID로 조회
     */
    public FileInfo findById(int uploadId) throws SQLException {
        String sql = "SELECT upload_id, user_id, original_filename, stored_filename, file_path, " +
                     "file_size, content_type, uploaded_at FROM uploads WHERE upload_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, uploadId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFileInfo(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 사용자의 모든 파일 조회
     */
    public List<FileInfo> findByUserId(int userId) throws SQLException {
        List<FileInfo> files = new ArrayList<>();
        String sql = "SELECT upload_id, user_id, original_filename, stored_filename, file_path, " +
                     "file_size, content_type, uploaded_at FROM uploads WHERE user_id = ? " +
                     "ORDER BY uploaded_at DESC";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    files.add(mapResultSetToFileInfo(rs));
                }
            }
        }
        return files;
    }
    
    /**
     * 파일 삭제
     */
    public boolean delete(int uploadId) throws SQLException {
        String sql = "DELETE FROM uploads WHERE upload_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, uploadId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * 사용자의 파일 삭제 (권한 체크 포함)
     */
    public boolean deleteByUser(int uploadId, int userId) throws SQLException {
        String sql = "DELETE FROM uploads WHERE upload_id = ? AND user_id = ?";
        
        try (Connection conn = DataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, uploadId);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * ResultSet을 FileInfo 객체로 변환
     */
    private FileInfo mapResultSetToFileInfo(ResultSet rs) throws SQLException {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUploadId(rs.getInt("upload_id"));
        fileInfo.setUserId(rs.getInt("user_id"));
        fileInfo.setOriginalFilename(rs.getString("original_filename"));
        fileInfo.setStoredFilename(rs.getString("stored_filename"));
        fileInfo.setFilePath(rs.getString("file_path"));
        fileInfo.setFileSize(rs.getLong("file_size"));
        fileInfo.setContentType(rs.getString("content_type"));
        
        Timestamp uploadedAt = rs.getTimestamp("uploaded_at");
        if (uploadedAt != null) {
            fileInfo.setUploadedAt(uploadedAt.toLocalDateTime());
        }
        
        return fileInfo;
    }
}

