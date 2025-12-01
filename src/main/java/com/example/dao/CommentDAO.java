package com.example.dao;

import com.example.model.PostComment;
import com.example.util.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 댓글 DAO
 */
public class CommentDAO {
    
    /**
     * 게시글의 댓글 목록 조회
     */
    public List<PostComment> findByPostId(int postId) throws SQLException {
        List<PostComment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username, u.full_name " +
                     "FROM post_comment c " +
                     "LEFT JOIN users u ON c.user_id = u.user_id " +
                     "WHERE c.post_id = ? " +
                     "ORDER BY c.created_at ASC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return comments;
    }
    
    /**
     * 댓글 작성
     */
    public int insert(PostComment comment) throws SQLException {
        String sql = "INSERT INTO post_comment (post_id, user_id, content, parent_comment_id) " +
                     "VALUES (?, ?, ?, ?) RETURNING comment_id";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, comment.getPostId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
            if (comment.getParentCommentId() != null) {
                pstmt.setInt(4, comment.getParentCommentId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("comment_id");
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return 0;
    }
    
    /**
     * 댓글 수정
     */
    public boolean update(PostComment comment) throws SQLException {
        String sql = "UPDATE post_comment SET content = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE comment_id = ? AND user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getCommentId());
            pstmt.setInt(3, comment.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 댓글 삭제
     */
    public boolean delete(int commentId, int userId) throws SQLException {
        String sql = "DELETE FROM post_comment WHERE comment_id = ? AND user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, commentId);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * ResultSet을 PostComment 객체로 변환
     */
    private PostComment mapResultSetToComment(ResultSet rs) throws SQLException {
        PostComment comment = new PostComment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setPostId(rs.getInt("post_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setContent(rs.getString("content"));
        
        Integer parentId = rs.getObject("parent_comment_id", Integer.class);
        comment.setParentCommentId(parentId);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            comment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            comment.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // 조인 필드
        if (rs.getMetaData().getColumnCount() > 7) {
            comment.setUsername(rs.getString("username"));
            comment.setFullName(rs.getString("full_name"));
        }
        
        return comment;
    }
}

