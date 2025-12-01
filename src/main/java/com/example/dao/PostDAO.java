package com.example.dao;

import com.example.model.Post;
import com.example.util.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 DAO
 */
public class PostDAO {
    
    /**
     * 전체 게시글 목록 조회 (페이징, 정렬)
     */
    public List<Post> findAll(String sortBy, String category, int page, int size) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.username, u.full_name " +
                     "FROM post p " +
                     "LEFT JOIN users u ON p.user_id = u.user_id " +
                     "WHERE 1=1";
        
        List<String> conditions = new ArrayList<>();
        if (category != null && !category.isEmpty() && !category.equals("전체글")) {
            conditions.add("p.category = ?");
        }
        
        if (conditions.size() > 0) {
            sql += " AND " + String.join(" AND ", conditions);
        }
        
        // 정렬
        if ("인기글".equals(sortBy) || "추천순".equals(sortBy)) {
            sql += " ORDER BY p.like_count DESC, p.created_at DESC";
        } else if ("조회순".equals(sortBy)) {
            sql += " ORDER BY p.view_count DESC, p.created_at DESC";
        } else {
            // 기본: 최신순 (공지글 상단 고정)
            sql += " ORDER BY p.is_pinned DESC, p.is_notice DESC, p.created_at DESC";
        }
        
        sql += " LIMIT ? OFFSET ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            if (category != null && !category.isEmpty() && !category.equals("전체글")) {
                pstmt.setString(paramIndex++, category);
            }
            pstmt.setInt(paramIndex++, size);
            pstmt.setInt(paramIndex++, (page - 1) * size);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return posts;
    }
    
    /**
     * 게시글 ID로 조회
     */
    public Post findById(int postId) throws SQLException {
        String sql = "SELECT p.*, u.username, u.full_name " +
                     "FROM post p " +
                     "LEFT JOIN users u ON p.user_id = u.user_id " +
                     "WHERE p.post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPost(rs);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return null;
    }
    
    /**
     * 게시글 작성
     */
    public int insert(Post post) throws SQLException {
        String sql = "INSERT INTO post (user_id, category, prefix, title, content, is_notice, is_pinned) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING post_id";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getCategory());
            pstmt.setString(3, post.getPrefix());
            pstmt.setString(4, post.getTitle());
            pstmt.setString(5, post.getContent());
            pstmt.setBoolean(6, post.isNotice());
            pstmt.setBoolean(7, post.isPinned());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("post_id");
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
        
        return 0;
    }
    
    /**
     * 게시글 수정
     */
    public boolean update(Post post) throws SQLException {
        String sql = "UPDATE post SET category = ?, prefix = ?, title = ?, content = ?, " +
                     "updated_at = CURRENT_TIMESTAMP WHERE post_id = ? AND user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, post.getCategory());
            pstmt.setString(2, post.getPrefix());
            pstmt.setString(3, post.getTitle());
            pstmt.setString(4, post.getContent());
            pstmt.setInt(5, post.getPostId());
            pstmt.setInt(6, post.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 게시글 삭제
     */
    public boolean delete(int postId, int userId) throws SQLException {
        String sql = "DELETE FROM post WHERE post_id = ? AND user_id = ?";
        
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
     * 조회수 증가
     */
    public void incrementViewCount(int postId) throws SQLException {
        String sql = "UPDATE post SET view_count = view_count + 1 WHERE post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 추천수 증가
     */
    public void incrementLikeCount(int postId) throws SQLException {
        String sql = "UPDATE post SET like_count = like_count + 1 WHERE post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 추천수 감소
     */
    public void decrementLikeCount(int postId) throws SQLException {
        String sql = "UPDATE post SET like_count = GREATEST(like_count - 1, 0) WHERE post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 댓글수 업데이트
     */
    public void updateCommentCount(int postId) throws SQLException {
        String sql = "UPDATE post SET comment_count = " +
                     "(SELECT COUNT(*) FROM post_comment WHERE post_id = ?) " +
                     "WHERE post_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 게시글 수 조회
     */
    public int getTotalCount(String category) throws SQLException {
        String sql = "SELECT COUNT(*) FROM post WHERE 1=1";
        
        if (category != null && !category.isEmpty() && !category.equals("전체글")) {
            sql += " AND category = ?";
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            if (category != null && !category.isEmpty() && !category.equals("전체글")) {
                pstmt.setString(1, category);
            }
            
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
    
    /**
     * ResultSet을 Post 객체로 변환
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setPostId(rs.getInt("post_id"));
        post.setUserId(rs.getInt("user_id"));
        post.setCategory(rs.getString("category"));
        post.setPrefix(rs.getString("prefix"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setViewCount(rs.getInt("view_count"));
        post.setLikeCount(rs.getInt("like_count"));
        post.setCommentCount(rs.getInt("comment_count"));
        post.setNotice(rs.getBoolean("is_notice"));
        post.setPinned(rs.getBoolean("is_pinned"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            post.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            post.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // 조인 필드
        if (rs.getMetaData().getColumnCount() > 12) {
            post.setUsername(rs.getString("username"));
            post.setFullName(rs.getString("full_name"));
        }
        
        return post;
    }
}

