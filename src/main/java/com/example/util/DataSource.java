package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 데이터베이스 연결 유틸리티 클래스
 * PostgreSQL 연결을 관리합니다.
 */
public class DataSource {
    
    // 환경 변수에서 DB 설정 읽기 (Docker 환경)
    private static final String DB_HOST = System.getenv("DB_HOST") != null 
        ? System.getenv("DB_HOST") : "localhost";
    private static final String DB_PORT = System.getenv("DB_PORT") != null 
        ? System.getenv("DB_PORT") : "5432";
    private static final String DB_NAME = System.getenv("DB_NAME") != null 
        ? System.getenv("DB_NAME") : "jsp_hw";
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") : "jsp_user";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") : "jsp_password";
    
    private static final String JDBC_URL = String.format(
        "jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME
    );
    
    // JDBC 드라이버 로드
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver를 찾을 수 없습니다.", e);
        }
    }
    
    /**
     * 데이터베이스 연결을 가져옵니다.
     * 
     * @return Connection 객체
     * @throws SQLException 연결 실패 시
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * 연결을 안전하게 닫습니다.
     * 
     * @param conn 닫을 Connection 객체 (null 가능)
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("연결 종료 중 오류 발생: " + e.getMessage());
            }
        }
    }
    
    /**
     * 연결 테스트 메서드
     * 
     * @return 연결 성공 여부
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("연결 테스트 실패: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}

