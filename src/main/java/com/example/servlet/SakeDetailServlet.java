package com.example.servlet;

import com.example.dao.ReviewDAO;
import com.example.dao.SakeDAO;
import com.example.model.Sake;
import com.example.model.SakeReview;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 사케 상세 서블릿
 */
@WebServlet("/sake/detail")
public class SakeDetailServlet extends HttpServlet {
    
    private SakeDAO sakeDAO;
    private ReviewDAO reviewDAO;
    
    @Override
    public void init() throws ServletException {
        sakeDAO = new SakeDAO();
        reviewDAO = new ReviewDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sakeIdParam = request.getParameter("sakeId");
        
        if (sakeIdParam == null || sakeIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
            return;
        }
        
        try {
            int sakeId = Integer.parseInt(sakeIdParam);
            
            // 사케 정보 조회
            Sake sake = sakeDAO.findById(sakeId);
            
            if (sake == null) {
                request.setAttribute("error", "사케를 찾을 수 없습니다.");
                request.getRequestDispatcher("/jsp/sake/list.jsp").forward(request, response);
                return;
            }
            
            // 리뷰 통계 조회
            Double averageRating = reviewDAO.getAverageRating(sakeId);
            int reviewCount = reviewDAO.getReviewCount(sakeId);
            
            // 리뷰 목록 조회 (페이징은 나중에 추가 가능)
            List<SakeReview> reviews = reviewDAO.findBySakeId(sakeId);
            
            request.setAttribute("sake", sake);
            request.setAttribute("averageRating", averageRating != null ? averageRating : 0.0);
            request.setAttribute("reviewCount", reviewCount);
            request.setAttribute("reviews", reviews);
            
            request.getRequestDispatcher("/jsp/sake/detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/sake/detail.jsp").forward(request, response);
        }
    }
}

