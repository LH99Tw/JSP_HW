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
 * 리뷰 수정 서블릿
 */
@WebServlet("/review/update")
public class ReviewUpdateServlet extends HttpServlet {
    
    private ReviewDAO reviewDAO;
    
    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String reviewIdParam = request.getParameter("reviewId");
        String sakeIdParam = request.getParameter("sakeId");
        
        if (reviewIdParam == null || sakeIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
            return;
        }
        
        try {
            int reviewId = Integer.parseInt(reviewIdParam);
            int sakeId = Integer.parseInt(sakeIdParam);
            
            // 리뷰 조회 및 작성자 확인
            SakeReview review = reviewDAO.findById(reviewId);
            if (review == null || review.getUserId() != user.getUserId()) {
                request.setAttribute("error", "수정 권한이 없습니다.");
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeId);
                return;
            }
            
            // 파라미터 받기
            String ratingParam = request.getParameter("rating");
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String sweetnessScoreParam = request.getParameter("sweetnessScore");
            String drynessScoreParam = request.getParameter("drynessScore");
            String acidityScoreParam = request.getParameter("acidityScore");
            String aromaScoreParam = request.getParameter("aromaScore");
            String bodyScoreParam = request.getParameter("bodyScore");
            
            // 유효성 검사
            if (ratingParam == null || content == null || content.trim().isEmpty() || content.length() < 10) {
                request.setAttribute("error", "필수 항목을 입력해주세요.");
                response.sendRedirect(request.getContextPath() + "/review/edit?reviewId=" + reviewId);
                return;
            }
            
            // 리뷰 정보 업데이트
            review.setRating(Integer.parseInt(ratingParam));
            review.setTitle(title != null ? title.trim() : null);
            review.setContent(content.trim());
            
            if (sweetnessScoreParam != null && !sweetnessScoreParam.isEmpty()) {
                review.setSweetnessScore(Integer.parseInt(sweetnessScoreParam));
            }
            if (drynessScoreParam != null && !drynessScoreParam.isEmpty()) {
                review.setDrynessScore(Integer.parseInt(drynessScoreParam));
            }
            if (acidityScoreParam != null && !acidityScoreParam.isEmpty()) {
                review.setAcidityScore(Integer.parseInt(acidityScoreParam));
            }
            if (aromaScoreParam != null && !aromaScoreParam.isEmpty()) {
                review.setAromaScore(Integer.parseInt(aromaScoreParam));
            }
            if (bodyScoreParam != null && !bodyScoreParam.isEmpty()) {
                review.setBodyScore(Integer.parseInt(bodyScoreParam));
            }
            
            // 업데이트
            boolean success = reviewDAO.update(review);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeId);
            } else {
                request.setAttribute("error", "리뷰 수정에 실패했습니다.");
                response.sendRedirect(request.getContextPath() + "/review/edit?reviewId=" + reviewId);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeIdParam);
        }
    }
}

