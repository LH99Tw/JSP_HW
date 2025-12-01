package com.example.util;

import com.example.model.User;

import javax.servlet.http.HttpSession;

/**
 * 세션 관리 유틸리티 클래스
 */
public class SessionUtil {
    
    private static final String USER_SESSION_KEY = "user";
    private static final String USER_ID_SESSION_KEY = "userId";
    private static final String USERNAME_SESSION_KEY = "username";
    private static final String ROLE_SESSION_KEY = "role";
    
    /**
     * 세션에서 사용자 객체 가져오기
     */
    public static User getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(USER_SESSION_KEY);
    }
    
    /**
     * 세션에 사용자 저장
     */
    public static void setUser(HttpSession session, User user) {
        if (session != null && user != null) {
            session.setAttribute(USER_SESSION_KEY, user);
            session.setAttribute(USER_ID_SESSION_KEY, user.getUserId());
            session.setAttribute(USERNAME_SESSION_KEY, user.getUsername());
            session.setAttribute(ROLE_SESSION_KEY, user.getRole());
        }
    }
    
    /**
     * 로그인 여부 확인
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getUser(session) != null;
    }
    
    /**
     * 관리자 여부 확인
     */
    public static boolean isAdmin(HttpSession session) {
        User user = getUser(session);
        return user != null && user.isAdmin();
    }
    
    /**
     * 세션에서 사용자 ID 가져오기
     */
    public static Integer getUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object userId = session.getAttribute(USER_ID_SESSION_KEY);
        if (userId instanceof Integer) {
            return (Integer) userId;
        }
        return null;
    }
    
    /**
     * 세션에서 사용자명 가져오기
     */
    public static String getUsername(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(USERNAME_SESSION_KEY);
    }
    
    /**
     * 세션에서 역할 가져오기
     */
    public static String getRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(ROLE_SESSION_KEY);
    }
    
    /**
     * 세션 무효화 (로그아웃)
     */
    public static void invalidate(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}

