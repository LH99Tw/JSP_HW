package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.Sake;
import com.example.model.User;
import com.example.util.MLClient;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 사케 추천 서블릿
 */
@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크 (선택사항 - 비로그인 사용자도 추천 가능)
        User user = SessionUtil.getUser(request.getSession(false));
        
        try {
            SakeDAO sakeDAO = new SakeDAO();
            List<Sake> recommendedSakes = new ArrayList<>();
            
            if (user != null) {
                // 로그인한 사용자: ML 서비스 기반 추천
                try {
                    List<Integer> recommendedSakeIds = MLClient.recommendSake(user.getUserId(), null);
                    
                    for (Integer sakeId : recommendedSakeIds) {
                        Sake sake = sakeDAO.findById(sakeId);
                        if (sake != null) {
                            recommendedSakes.add(sake);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("ML 서비스 추천 실패: " + e.getMessage());
                    // ML 서비스 실패 시 인기 사케로 대체
                    recommendedSakes = sakeDAO.findPopularSakes(10);
                }
            } else {
                // 비로그인 사용자: 인기 사케 표시
                recommendedSakes = sakeDAO.findPopularSakes(10);
            }
            
            // request에 설정
            request.setAttribute("sakes", recommendedSakes);
            request.setAttribute("isRecommended", user != null);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/recommend/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "추천 사케를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

