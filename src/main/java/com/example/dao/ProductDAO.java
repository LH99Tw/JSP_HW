package com.example.dao;

import com.example.model.SakeProduct;
import com.example.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 사케 상품 데이터 접근 객체
 */
public class ProductDAO {
    
    /**
     * 전체 상품 목록 조회 (노출된 상품만, 사케 정보 포함)
     */
    public List<SakeProduct> findAll() throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "WHERE sp.is_published = true ORDER BY sp.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProductWithSake(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 상품 목록 조회 (관리자용 - 모든 상품, 사케 정보 포함)
     */
    public List<SakeProduct> findAllWithSakeInfo() throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "ORDER BY sp.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProductWithSake(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 상품 ID로 상세 조회 (사케 정보 포함) - 관리자용
     */
    public SakeProduct findByIdWithSakeInfo(int productId) throws SQLException {
        return findById(productId);
    }
    
    /**
     * 지역별 상품 조회
     */
    public List<SakeProduct> findByRegion(String region) throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "WHERE sp.is_published = true AND s.region_prefecture = ? ORDER BY sp.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, region);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProductWithSake(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 스타일별 상품 조회
     */
    public List<SakeProduct> findByStyle(String style) throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "WHERE sp.is_published = true AND s.style = ? ORDER BY sp.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, style);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProductWithSake(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 검색 (상품명 또는 브랜드)
     */
    public List<SakeProduct> search(String keyword) throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "WHERE sp.is_published = true AND (s.name_ko LIKE ? OR s.brand LIKE ?) " +
                     "ORDER BY sp.created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProductWithSake(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 상품 ID로 상세 조회 (사케 정보 포함)
     */
    public SakeProduct findById(int productId) throws SQLException {
        String sql = "SELECT sp.*, s.name_ko as sake_name, s.brand, s.region_prefecture as region, s.style, s.thumbnail_path as image_url " +
                     "FROM sake_product sp " +
                     "JOIN sake s ON sp.sake_id = s.sake_id " +
                     "WHERE sp.product_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProductWithSake(rs);
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 사케별 상품 조회
     */
    public List<SakeProduct> findBySakeId(int sakeId) throws SQLException {
        String sql = "SELECT * FROM sake_product WHERE sake_id = ? AND is_published = true ORDER BY created_at DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sakeId);
            rs = pstmt.executeQuery();
            
            List<SakeProduct> productList = new ArrayList<>();
            while (rs.next()) {
                productList.add(mapResultSetToProduct(rs));
            }
            return productList;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 상품 등록
     */
    public int insert(SakeProduct product) throws SQLException {
        String sql = "INSERT INTO sake_product (sake_id, price, stock, is_published, label) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, product.getSakeId());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setBoolean(4, product.isPublished());
            pstmt.setString(5, product.getLabel());
            
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
     * 상품 정보 수정
     */
    public boolean update(SakeProduct product) throws SQLException {
        String sql = "UPDATE sake_product SET price = ?, stock = ?, is_published = ?, label = ? " +
                     "WHERE product_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getPrice());
            pstmt.setInt(2, product.getStock());
            pstmt.setBoolean(3, product.isPublished());
            pstmt.setString(4, product.getLabel());
            pstmt.setInt(5, product.getProductId());
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 상품 삭제
     */
    public boolean delete(int productId) throws SQLException {
        String sql = "DELETE FROM sake_product WHERE product_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 재고 업데이트
     */
    public boolean updateStock(int productId, int quantity) throws SQLException {
        String sql = "UPDATE sake_product SET stock = stock + ? WHERE product_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) pstmt.close();
            DataSource.closeConnection(conn);
        }
    }
    
    /**
     * 전체 상품 수 조회
     */
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sake_product WHERE is_published = true";
        
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
     * ResultSet을 SakeProduct 객체로 변환 (사케 정보 포함)
     */
    private SakeProduct mapResultSetToProductWithSake(ResultSet rs) throws SQLException {
        SakeProduct product = new SakeProduct();
        product.setProductId(rs.getInt("product_id"));
        product.setSakeId(rs.getInt("sake_id"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setPublished(rs.getBoolean("is_published"));
        product.setLabel(rs.getString("label"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        // 사케 정보 추가
        product.setSakeName(rs.getString("sake_name"));
        product.setBrand(rs.getString("brand"));
        product.setRegion(rs.getString("region"));
        product.setStyle(rs.getString("style"));
        product.setImageUrl(rs.getString("image_url"));
        return product;
    }
    
    /**
     * ResultSet을 SakeProduct 객체로 변환
     */
    private SakeProduct mapResultSetToProduct(ResultSet rs) throws SQLException {
        SakeProduct product = new SakeProduct();
        product.setProductId(rs.getInt("product_id"));
        product.setSakeId(rs.getInt("sake_id"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setPublished(rs.getBoolean("is_published"));
        product.setLabel(rs.getString("label"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        return product;
    }
}

