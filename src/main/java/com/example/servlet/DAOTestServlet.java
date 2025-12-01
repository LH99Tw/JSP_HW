package com.example.servlet;

import com.example.dao.*;
import com.example.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO 레이어 테스트 서블릿
 * 개발용으로만 사용합니다.
 */
@WebServlet("/test/dao")
public class DAOTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>DAO 테스트</title>");
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
        out.println("<h1>DAO 레이어 테스트</h1>");
        
        // SakeDAO 테스트
        List<Sake> sakeList = new java.util.ArrayList<>();
        out.println("<h2>1. SakeDAO 테스트</h2>");
        try {
            SakeDAO sakeDAO = new SakeDAO();
            sakeList = sakeDAO.findAll();
            out.println("<p class='success'>✅ 전체 사케 조회 성공: " + sakeList.size() + "개</p>");
            
            if (!sakeList.isEmpty()) {
                Sake firstSake = sakeList.get(0);
                out.println("<p>첫 번째 사케: " + firstSake.getDisplayName() + " (ID: " + firstSake.getSakeId() + ")</p>");
                
                // 상세 조회 테스트
                Sake detailSake = sakeDAO.findById(firstSake.getSakeId());
                if (detailSake != null) {
                    out.println("<p class='success'>✅ 사케 상세 조회 성공</p>");
                }
            }
        } catch (SQLException e) {
            out.println("<p class='error'>❌ SakeDAO 오류: " + e.getMessage() + "</p>");
        }
        
        // ReviewDAO 테스트
        out.println("<h2>2. ReviewDAO 테스트</h2>");
        try {
            ReviewDAO reviewDAO = new ReviewDAO();
            if (!sakeList.isEmpty()) {
                int sakeId = sakeList.get(0).getSakeId();
                Double avgRating = reviewDAO.getAverageRating(sakeId);
                int reviewCount = reviewDAO.getReviewCount(sakeId);
                out.println("<p class='success'>✅ 평균 평점: " + avgRating + ", 리뷰 수: " + reviewCount + "</p>");
            }
        } catch (SQLException e) {
            out.println("<p class='error'>❌ ReviewDAO 오류: " + e.getMessage() + "</p>");
        }
        
        // UserDAO 테스트
        out.println("<h2>3. UserDAO 테스트</h2>");
        try {
            UserDAO userDAO = new UserDAO();
            User admin = userDAO.findByUsername("admin");
            if (admin != null) {
                out.println("<p class='success'>✅ 관리자 계정 조회 성공: " + admin.getUsername() + " (역할: " + admin.getRole() + ")</p>");
            } else {
                out.println("<p class='error'>❌ 관리자 계정을 찾을 수 없습니다.</p>");
            }
        } catch (SQLException e) {
            out.println("<p class='error'>❌ UserDAO 오류: " + e.getMessage() + "</p>");
        }
        
        // ProductDAO 테스트
        out.println("<h2>4. ProductDAO 테스트</h2>");
        try {
            ProductDAO productDAO = new ProductDAO();
            List<SakeProduct> productList = productDAO.findAll();
            out.println("<p class='success'>✅ 전체 상품 조회 성공: " + productList.size() + "개</p>");
        } catch (SQLException e) {
            out.println("<p class='error'>❌ ProductDAO 오류: " + e.getMessage() + "</p>");
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}

