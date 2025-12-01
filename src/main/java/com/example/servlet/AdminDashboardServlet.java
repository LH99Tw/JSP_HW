package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.dao.ReviewDAO;
import com.example.dao.ProductDAO;
import com.example.dao.OrderDAO;
import com.example.model.User;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 관리자 대시보드 서블릿
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            SakeDAO sakeDAO = new SakeDAO();
            ReviewDAO reviewDAO = new ReviewDAO();
            ProductDAO productDAO = new ProductDAO();
            OrderDAO orderDAO = new OrderDAO();
            
            // 통계 데이터 조회
            int sakeCount = sakeDAO.getTotalCount();
            int reviewCount = reviewDAO.getTotalCount();
            int productCount = productDAO.getTotalCount();
            int todayOrderCount = orderDAO.getTodayOrderCount();
            
            // 인기 사케 TOP 10 (리뷰 수 기준)
            List<com.example.model.Sake> popularSakes = sakeDAO.findPopularSakes(10);
            
            // request에 설정
            request.setAttribute("sakeCount", sakeCount);
            request.setAttribute("reviewCount", reviewCount);
            request.setAttribute("productCount", productCount);
            request.setAttribute("todayOrderCount", todayOrderCount);
            request.setAttribute("popularSakes", popularSakes);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "대시보드 데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

