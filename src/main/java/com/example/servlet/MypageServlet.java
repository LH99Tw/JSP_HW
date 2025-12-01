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
 * 마이페이지 서블릿
 */
@WebServlet("/mypage")
public class MypageServlet extends HttpServlet {
    
    private ReviewDAO reviewDAO;
    
    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크 (필터에서 처리되지만 추가 안전장치)
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // 사용자 리뷰 목록 조회
            request.setAttribute("reviews", reviewDAO.findByUserId(user.getUserId()));
            
            // 마이페이지로 포워드
            request.getRequestDispatcher("/jsp/mypage/index.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/mypage/index.jsp").forward(request, response);
        }
    }
}

