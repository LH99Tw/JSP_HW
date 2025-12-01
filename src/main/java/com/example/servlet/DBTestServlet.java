package com.example.servlet;

import com.example.util.DataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 데이터베이스 연결 테스트 서블릿
 * 개발용으로만 사용합니다.
 */
@WebServlet("/test/db")
public class DBTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>DB 연결 테스트</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>데이터베이스 연결 테스트</h1>");
        
        Connection conn = null;
        try {
            // 연결 테스트
            out.println("<h2>1. 연결 테스트</h2>");
            conn = DataSource.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                out.println("<p class='success'>✅ 데이터베이스 연결 성공!</p>");
                
                // 데이터베이스 정보
                DatabaseMetaData metaData = conn.getMetaData();
                out.println("<h2>2. 데이터베이스 정보</h2>");
                out.println("<ul>");
                out.println("<li>DB 제품: " + metaData.getDatabaseProductName() + "</li>");
                out.println("<li>DB 버전: " + metaData.getDatabaseProductVersion() + "</li>");
                out.println("<li>드라이버: " + metaData.getDriverName() + "</li>");
                out.println("<li>드라이버 버전: " + metaData.getDriverVersion() + "</li>");
                out.println("</ul>");
                
                // 테이블 목록 확인
                out.println("<h2>3. 테이블 목록</h2>");
                ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                
                out.println("<table>");
                out.println("<tr><th>테이블명</th><th>타입</th></tr>");
                
                int tableCount = 0;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String tableType = tables.getString("TABLE_TYPE");
                    out.println("<tr><td>" + tableName + "</td><td>" + tableType + "</td></tr>");
                    tableCount++;
                }
                out.println("</table>");
                out.println("<p>총 " + tableCount + "개의 테이블이 있습니다.</p>");
                
                // 사케 도메인 테이블 확인
                out.println("<h2>4. 사케 도메인 테이블 확인</h2>");
                String[] domainTables = {"users", "sake", "sake_review", "sake_product", "cart_item", "orders", "order_item"};
                out.println("<ul>");
                for (String tableName : domainTables) {
                    ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
                    if (rs.next()) {
                        out.println("<li class='success'>✅ " + tableName + " 테이블 존재</li>");
                    } else {
                        out.println("<li class='error'>❌ " + tableName + " 테이블 없음</li>");
                    }
                }
                out.println("</ul>");
                
            } else {
                out.println("<p class='error'>❌ 데이터베이스 연결 실패</p>");
            }
            
        } catch (SQLException e) {
            out.println("<p class='error'>❌ 오류 발생: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(new PrintWriter(out));
            out.println("</pre>");
        } finally {
            DataSource.closeConnection(conn);
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}

