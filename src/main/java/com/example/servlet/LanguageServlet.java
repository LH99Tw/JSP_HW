package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * 언어 변경 서블릿
 */
@WebServlet("/language/change")
public class LanguageServlet extends HttpServlet {
    
    private static final String LANG_COOKIE_NAME = "lang";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30일
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String lang = request.getParameter("lang");
        String redirect = request.getParameter("redirect");
        String referer = request.getHeader("Referer");
        
        // 지원하는 언어: ko, en, ja
        if (lang == null || (!lang.equals("ko") && !lang.equals("en") && !lang.equals("ja"))) {
            lang = "ko"; // 기본값
        }
        
        // 쿠키에 언어 저장
        Cookie langCookie = new Cookie(LANG_COOKIE_NAME, lang);
        langCookie.setMaxAge(COOKIE_MAX_AGE);
        langCookie.setPath("/");
        response.addCookie(langCookie);
        
        // 세션에도 저장
        request.getSession().setAttribute("lang", lang);
        
        // 리다이렉트 URL 결정
        String redirectUrl;
        if (redirect != null && !redirect.isEmpty()) {
            redirectUrl = redirect;
        } else if (referer != null && !referer.isEmpty()) {
            redirectUrl = referer;
        } else {
            redirectUrl = request.getContextPath() + "/";
        }
        
        response.sendRedirect(redirectUrl);
    }
}

