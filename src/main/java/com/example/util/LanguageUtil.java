package com.example.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 언어 설정 유틸리티 클래스
 */
public class LanguageUtil {
    
    private static final String LANG_COOKIE_NAME = "lang";
    private static final String LANG_SESSION_KEY = "lang";
    private static final String DEFAULT_LANG = "ko";
    
    /**
     * 요청에서 언어 코드 가져오기 (쿠키 > 세션 > 기본값 순서)
     */
    public static String getLanguage(HttpServletRequest request) {
        // 1. 쿠키에서 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LANG_COOKIE_NAME.equals(cookie.getName())) {
                    String lang = cookie.getValue();
                    if (isValidLanguage(lang)) {
                        return lang;
                    }
                }
            }
        }
        
        // 2. 세션에서 확인
        HttpSession session = request.getSession(false);
        if (session != null) {
            String lang = (String) session.getAttribute(LANG_SESSION_KEY);
            if (isValidLanguage(lang)) {
                return lang;
            }
        }
        
        // 3. 기본값 반환
        return DEFAULT_LANG;
    }
    
    /**
     * 유효한 언어 코드인지 확인
     */
    private static boolean isValidLanguage(String lang) {
        return lang != null && (lang.equals("ko") || lang.equals("en") || lang.equals("ja"));
    }
    
    /**
     * 언어 코드를 리소스 번들 이름으로 변환
     */
    public static String getResourceBundleName(String lang) {
        return "messages_" + lang;
    }
}

