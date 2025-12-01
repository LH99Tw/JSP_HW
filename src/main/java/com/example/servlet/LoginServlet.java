package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.PasswordUtil;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 이미 로그인한 경우 홈으로 리다이렉트
        if (SessionUtil.isLoggedIn(request.getSession(false))) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // 성공 메시지 처리
        String success = request.getParameter("success");
        if ("register".equals(success)) {
            request.setAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
        }
        
        // 로그인 페이지로 포워드
        request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        String redirect = request.getParameter("redirect");
        
        // 입력값 검증
        if (username == null || username.trim().isEmpty() || 
            password == null || password.isEmpty()) {
            request.setAttribute("error", "사용자명과 비밀번호를 입력해주세요.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            return;
        }
        
        try {
            UserDAO userDAO = new UserDAO();
            // 사용자 조회
            User user = userDAO.findByUsername(username);
            
            if (user == null) {
                request.setAttribute("error", "사용자명 또는 비밀번호가 올바르지 않습니다.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
                return;
            }
            
            // 비밀번호 검증
            if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                request.setAttribute("error", "사용자명 또는 비밀번호가 올바르지 않습니다.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
                return;
            }
            
            // 세션에 사용자 정보 저장
            SessionUtil.setUser(request.getSession(), user);
            
            // "아이디 저장" 체크 시 쿠키 발급
            if ("on".equals(rememberMe)) {
                Cookie cookie = new Cookie("rememberedUsername", username);
                cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                // 쿠키 삭제
                Cookie cookie = new Cookie("rememberedUsername", "");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            
            // 리다이렉트 처리
            if (redirect != null && !redirect.isEmpty()) {
                response.sendRedirect(request.getContextPath() + redirect);
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

