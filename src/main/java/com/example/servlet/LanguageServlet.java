package com.example.servlet;

import com.example.util.LanguageUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 언어 변경 서블릿
 */
@WebServlet("/language")
public class LanguageServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String language = request.getParameter("lang");
        
        if (language != null && (language.equals("ko") || language.equals("en") || language.equals("ja"))) {
            // 세션에 저장
            LanguageUtil.setLanguage(request, language);
            
            // 쿠키에 저장
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("language", language);
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1년
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
        }
        
        // 이전 페이지로 리다이렉트
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}

