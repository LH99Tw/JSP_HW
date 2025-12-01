package com.example.servlet;

import com.example.dao.ReviewDAO;
import com.example.model.User;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 관리자 리뷰 삭제 서블릿
 */
@WebServlet("/admin/review/delete")
public class AdminReviewDeleteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String reviewIdParam = request.getParameter("reviewId");
        
        if (reviewIdParam == null || reviewIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/review/list");
            return;
        }
        
        try {
            int reviewId = Integer.parseInt(reviewIdParam);
            ReviewDAO reviewDAO = new ReviewDAO();
            boolean success = reviewDAO.delete(reviewId);
            
            response.sendRedirect(request.getContextPath() + "/admin/review/list");
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/review/list");
        }
    }
}

