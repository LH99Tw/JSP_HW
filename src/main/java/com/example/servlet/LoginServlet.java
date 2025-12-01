package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 로그인 서블릿
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 이미 로그인한 경우 메인으로 리다이렉트
        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // 유효성 검사
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "사용자명을 입력해주세요.");
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "비밀번호를 입력해주세요.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // 사용자 조회
            User user = userDAO.findByUsername(username);
            
            if (user == null || !user.getPassword().equals(password)) {
                request.setAttribute("error", "사용자명 또는 비밀번호가 올바르지 않습니다.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
                return;
            }
            
            // 로그인 성공 - 세션에 사용자 정보 저장
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("userId", user.getUserId());
            
            // Remember Me 처리 (쿠키)
            if ("on".equals(rememberMe)) {
                javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("rememberedUser", username);
                cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
                cookie.setPath(request.getContextPath());
                response.addCookie(cookie);
            }
            
            // 메인 페이지로 리다이렉트
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
        }
    }
}

