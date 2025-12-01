package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 회원가입 서블릿
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 회원가입 페이지로 포워드
        request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 파라미터 받기
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String fullName = request.getParameter("fullName");
        
        // 서버 측 유효성 검사
        String errorMessage = validateInput(username, email, password, passwordConfirm, fullName);
        
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // 이메일 중복 체크
            if (userDAO.findByEmail(email) != null) {
                request.setAttribute("error", "이미 사용 중인 이메일입니다.");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
                return;
            }
            
            // 사용자명 중복 체크
            if (userDAO.findByUsername(username) != null) {
                request.setAttribute("error", "이미 사용 중인 사용자명입니다.");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
                return;
            }
            
            // 비밀번호 해싱
            String hashedPassword = PasswordUtil.hashPassword(password);
            
            // 사용자 생성
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setFullName(fullName);
            user.setRole("USER");
            user.setLanguage("ko");
            
            // DB에 저장
            int userId = userDAO.insert(user);
            
            if (userId > 0) {
                // 성공 시 로그인 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/login?success=register");
            } else {
                request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
                request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
        }
    }
    
    /**
     * 입력값 유효성 검사
     */
    private String validateInput(String username, String email, String password, 
                                 String passwordConfirm, String fullName) {
        if (username == null || username.trim().isEmpty()) {
            return "사용자명을 입력해주세요.";
        }
        if (username.length() < 3 || username.length() > 50) {
            return "사용자명은 3자 이상 50자 이하여야 합니다.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "이메일을 입력해주세요.";
        }
        if (!isValidEmail(email)) {
            return "올바른 이메일 형식이 아닙니다.";
        }
        if (password == null || password.isEmpty()) {
            return "비밀번호를 입력해주세요.";
        }
        if (password.length() < 6) {
            return "비밀번호는 6자 이상이어야 합니다.";
        }
        if (!password.equals(passwordConfirm)) {
            return "비밀번호가 일치하지 않습니다.";
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            return "이름을 입력해주세요.";
        }
        return null;
    }
    
    /**
     * 이메일 형식 검증
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}

