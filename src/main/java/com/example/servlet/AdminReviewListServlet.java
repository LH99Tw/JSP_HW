package com.example.servlet;

import com.example.dao.ReviewDAO;
import com.example.model.SakeReview;
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
 * 관리자 리뷰 목록 서블릿
 */
@WebServlet("/admin/review/list")
public class AdminReviewListServlet extends HttpServlet {
    
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
            ReviewDAO reviewDAO = new ReviewDAO();
            
            // 파라미터 받기
            String sakeIdParam = request.getParameter("sakeId");
            String userIdParam = request.getParameter("userId");
            String ratingParam = request.getParameter("rating");
            
            // 리뷰 목록 조회
            List<SakeReview> reviews;
            if (sakeIdParam != null && !sakeIdParam.isEmpty()) {
                int sakeId = Integer.parseInt(sakeIdParam);
                reviews = reviewDAO.findBySakeId(sakeId);
            } else if (userIdParam != null && !userIdParam.isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                reviews = reviewDAO.findByUserId(userId);
            } else {
                reviews = reviewDAO.findAll();
            }
            
            // 평점 필터링
            if (ratingParam != null && !ratingParam.isEmpty()) {
                int rating = Integer.parseInt(ratingParam);
                reviews.removeIf(r -> r.getRating() != rating);
            }
            
            // request에 설정
            request.setAttribute("reviews", reviews);
            request.setAttribute("sakeId", sakeIdParam);
            request.setAttribute("userId", userIdParam);
            request.setAttribute("rating", ratingParam);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/review/list.jsp").forward(request, response);
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "리뷰 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

