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

/**
 * 리뷰 삭제 서블릿
 */
@WebServlet("/review/delete")
public class ReviewDeleteServlet extends HttpServlet {
    
    private ReviewDAO reviewDAO;
    
    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String reviewIdParam = request.getParameter("reviewId");
        
        if (reviewIdParam == null || reviewIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
            return;
        }
        
        try {
            int reviewId = Integer.parseInt(reviewIdParam);
            
            // 리뷰 조회 및 작성자 확인
            SakeReview review = reviewDAO.findById(reviewId);
            if (review == null) {
                response.sendRedirect(request.getContextPath() + "/sake/list");
                return;
            }
            
            // 작성자 본인 또는 관리자만 삭제 가능
            if (review.getUserId() != user.getUserId() && !user.isAdmin()) {
                request.setAttribute("error", "삭제 권한이 없습니다.");
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + review.getSakeId());
                return;
            }
            
            int sakeId = review.getSakeId();
            
            // 리뷰 삭제
            boolean success = reviewDAO.delete(reviewId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeId);
            } else {
                request.setAttribute("error", "리뷰 삭제에 실패했습니다.");
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeId);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/sake/list");
        }
    }
}

