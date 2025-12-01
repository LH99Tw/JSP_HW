package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 데이터베이스 연결 관리 클래스
 * 간단한 Connection Pool 구현
 */
public class DataSource {
    // Docker 환경 변수에서 읽거나 기본값 사용
    private static final String DB_HOST = System.getenv("DB_HOST") != null 
        ? System.getenv("DB_HOST") 
        : "localhost";
    private static final String DB_PORT = System.getenv("DB_PORT") != null 
        ? System.getenv("DB_PORT") 
        : "5432";
    private static final String DB_NAME = System.getenv("DB_NAME") != null 
        ? System.getenv("DB_NAME") 
        : "jsp_hw";
    private static final String DB_USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "jsp_user";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "jsp_password";
    
    // JDBC URL 구성
    private static final String DB_URL = String.format("jdbc:postgresql://%s:%s/%s", 
        DB_HOST, DB_PORT, DB_NAME);
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }
    
    /**
     * 데이터베이스 연결 가져오기
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

