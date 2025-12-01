package com.example.dao;

import com.example.model.Sake;
import com.example.util.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사케 데이터 접근 객체
 */
public class SakeDAO {
    
    /**
     * 전체 사케 목록 조회
     */
    public List<Sake> findAll() throws SQLException {
        String sql = "SELECT * FROM sake ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<Sake> sakeList = new ArrayList<>();
            while (rs.next()) {
                sakeList.add(mapResultSetToSake(rs));
            }
            return sakeList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케 ID로 상세 조회
     */
    public Sake findById(int sakeId) throws SQLException {
        String sql = "SELECT * FROM sake WHERE sake_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSake(rs);
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 지역별 사케 목록 조회
     */
    public List<Sake> findByRegion(String region) throws SQLException {
        String sql = "SELECT * FROM sake WHERE region_prefecture = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, region);
            rs = pstmt.executeQuery();
            
            List<Sake> sakeList = new ArrayList<>();
            while (rs.next()) {
                sakeList.add(mapResultSetToSake(rs));
            }
            return sakeList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 스타일별 사케 목록 조회
     */
    public List<Sake> findByStyle(String style) throws SQLException {
        String sql = "SELECT * FROM sake WHERE style = ? ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, style);
            rs = pstmt.executeQuery();
            
            List<Sake> sakeList = new ArrayList<>();
            while (rs.next()) {
                sakeList.add(mapResultSetToSake(rs));
            }
            return sakeList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 검색 (이름, 브랜드)
     */
    public List<Sake> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM sake WHERE " +
                     "name_ja ILIKE ? OR name_en ILIKE ? OR name_ko ILIKE ? OR " +
                     "brand ILIKE ? OR brewery ILIKE ? " +
                     "ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);
            rs = pstmt.executeQuery();
            
            List<Sake> sakeList = new ArrayList<>();
            while (rs.next()) {
                sakeList.add(mapResultSetToSake(rs));
            }
            return sakeList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케 등록
     */
    public int insert(Sake sake) throws SQLException {
        String sql = "INSERT INTO sake (name_ja, name_en, name_ko, brand, brewery, region_prefecture, " +
                     "style, alcohol_percent, volume_ml, polishing_ratio, nihonshu_do, acidity, " +
                     "sweetness_level, aroma_type, body_level, thumbnail_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, sake.getNameJa());
            pstmt.setString(2, sake.getNameEn());
            pstmt.setString(3, sake.getNameKo());
            pstmt.setString(4, sake.getBrand());
            pstmt.setString(5, sake.getBrewery());
            pstmt.setString(6, sake.getRegionPrefecture());
            pstmt.setString(7, sake.getStyle());
            pstmt.setBigDecimal(8, sake.getAlcoholPercent());
            pstmt.setObject(9, sake.getVolumeMl());
            pstmt.setObject(10, sake.getPolishingRatio());
            pstmt.setBigDecimal(11, sake.getNihonshuDo());
            pstmt.setBigDecimal(12, sake.getAcidity());
            pstmt.setObject(13, sake.getSweetnessLevel());
            pstmt.setString(14, sake.getAromaType());
            pstmt.setObject(15, sake.getBodyLevel());
            pstmt.setString(16, sake.getThumbnailPath());
            
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
     * 사케 정보 수정
     */
    public boolean update(Sake sake) throws SQLException {
        String sql = "UPDATE sake SET name_ja = ?, name_en = ?, name_ko = ?, brand = ?, brewery = ?, " +
                     "region_prefecture = ?, style = ?, alcohol_percent = ?, volume_ml = ?, " +
                     "polishing_ratio = ?, nihonshu_do = ?, acidity = ?, sweetness_level = ?, " +
                     "aroma_type = ?, body_level = ?, thumbnail_path = ? " +
                     "WHERE sake_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sake.getNameJa());
            pstmt.setString(2, sake.getNameEn());
            pstmt.setString(3, sake.getNameKo());
            pstmt.setString(4, sake.getBrand());
            pstmt.setString(5, sake.getBrewery());
            pstmt.setString(6, sake.getRegionPrefecture());
            pstmt.setString(7, sake.getStyle());
            pstmt.setBigDecimal(8, sake.getAlcoholPercent());
            pstmt.setObject(9, sake.getVolumeMl());
            pstmt.setObject(10, sake.getPolishingRatio());
            pstmt.setBigDecimal(11, sake.getNihonshuDo());
            pstmt.setBigDecimal(12, sake.getAcidity());
            pstmt.setObject(13, sake.getSweetnessLevel());
            pstmt.setString(14, sake.getAromaType());
            pstmt.setObject(15, sake.getBodyLevel());
            pstmt.setString(16, sake.getThumbnailPath());
            pstmt.setInt(17, sake.getSakeId());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케 삭제
     */
    public boolean delete(int sakeId) throws SQLException {
        String sql = "DELETE FROM sake WHERE sake_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 사케 수 조회
     */
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sake";
        
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
     * 인기 사케 TOP N 조회 (리뷰 수 기준)
     */
    public List<Sake> findPopularSakes(int limit) throws SQLException {
        String sql = "SELECT s.*, COUNT(sr.review_id) as review_count " +
                     "FROM sake s " +
                     "LEFT JOIN sake_review sr ON s.sake_id = sr.sake_id " +
                     "GROUP BY s.sake_id " +
                     "ORDER BY review_count DESC, s.created_at DESC " +
                     "LIMIT ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();
            
            List<Sake> sakeList = new ArrayList<>();
            while (rs.next()) {
                sakeList.add(mapResultSetToSake(rs));
            }
            return sakeList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * ResultSet을 Sake 객체로 변환
     */
    private Sake mapResultSetToSake(ResultSet rs) throws SQLException {
        Sake sake = new Sake();
        sake.setSakeId(rs.getInt("sake_id"));
        sake.setNameJa(rs.getString("name_ja"));
        sake.setNameEn(rs.getString("name_en"));
        sake.setNameKo(rs.getString("name_ko"));
        sake.setBrand(rs.getString("brand"));
        sake.setBrewery(rs.getString("brewery"));
        sake.setRegionPrefecture(rs.getString("region_prefecture"));
        sake.setStyle(rs.getString("style"));
        sake.setAlcoholPercent(rs.getBigDecimal("alcohol_percent"));
        sake.setVolumeMl(rs.getObject("volume_ml") != null ? rs.getInt("volume_ml") : null);
        sake.setPolishingRatio(rs.getObject("polishing_ratio") != null ? rs.getInt("polishing_ratio") : null);
        sake.setNihonshuDo(rs.getBigDecimal("nihonshu_do"));
        sake.setAcidity(rs.getBigDecimal("acidity"));
        sake.setSweetnessLevel(rs.getObject("sweetness_level") != null ? rs.getInt("sweetness_level") : null);
        sake.setAromaType(rs.getString("aroma_type"));
        sake.setBodyLevel(rs.getObject("body_level") != null ? rs.getInt("body_level") : null);
        sake.setThumbnailPath(rs.getString("thumbnail_path"));
        sake.setCreatedAt(rs.getTimestamp("created_at"));
        return sake;
    }
}

