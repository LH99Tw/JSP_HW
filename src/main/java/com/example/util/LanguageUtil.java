package com.example.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 다국어 지원 유틸리티 클래스
 */
public class LanguageUtil {
    
    private static final String DEFAULT_LANGUAGE = "ko";
    private static final String LANGUAGE_COOKIE_NAME = "language";
    private static final String LANGUAGE_SESSION_KEY = "language";
    
    /**
     * 현재 사용자의 언어 설정 가져오기
     */
    public static String getLanguage(HttpServletRequest request) {
        // 1. 세션에서 확인
        HttpSession session = request.getSession();
        String language = (String) session.getAttribute(LANGUAGE_SESSION_KEY);
        
        if (language != null && !language.isEmpty()) {
            return language;
        }
        
        // 2. 쿠키에서 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LANGUAGE_COOKIE_NAME.equals(cookie.getName())) {
                    language = cookie.getValue();
                    // 세션에도 저장
                    session.setAttribute(LANGUAGE_SESSION_KEY, language);
                    return language;
                }
            }
        }
        
        // 3. 기본값 반환
        return DEFAULT_LANGUAGE;
    }
    
    /**
     * 언어 설정 저장 (세션 및 쿠키)
     */
    public static void setLanguage(HttpServletRequest request, String language) {
        HttpSession session = request.getSession();
        session.setAttribute(LANGUAGE_SESSION_KEY, language);
    }
    
    /**
     * Locale 객체 가져오기
     */
    public static Locale getLocale(HttpServletRequest request) {
        String language = getLanguage(request);
        switch (language) {
            case "en":
                return Locale.ENGLISH;
            case "ja":
                return Locale.JAPANESE;
            default:
                return Locale.KOREAN;
        }
    }
    
    /**
     * ResourceBundle 가져오기
     */
    public static ResourceBundle getResourceBundle(HttpServletRequest request) {
        Locale locale = getLocale(request);
        return ResourceBundle.getBundle("i18n.messages", locale);
    }
}

