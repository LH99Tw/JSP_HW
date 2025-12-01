package com.example.filter;

import com.example.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 인증 필터
 * 로그인이 필요한 페이지에 접근 시 로그인 페이지로 리다이렉트
 */
public class AuthFilter implements Filter {
    
    // 로그인이 필요 없는 경로들
    private static final String[] EXCLUDED_PATHS = {
        "/login",
        "/register",
        "/test",
        "/css",
        "/images",
        "/js",
        "/uploads"
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 작업
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // 제외 경로 확인
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 로그인이 필요한 경로인지 확인
        if (isProtectedPath(path)) {
            HttpSession session = httpRequest.getSession(false);
            
            // 로그인하지 않은 경우
            if (!SessionUtil.isLoggedIn(session)) {
                // 원래 요청한 URL을 파라미터로 전달
                String redirectURL = contextPath + "/login?redirect=" + 
                    java.net.URLEncoder.encode(path, "UTF-8");
                httpResponse.sendRedirect(redirectURL);
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
    
    /**
     * 제외 경로인지 확인
     */
    private boolean isExcludedPath(String path) {
        for (String excluded : EXCLUDED_PATHS) {
            if (path.startsWith(excluded)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 보호된 경로인지 확인
     */
    private boolean isProtectedPath(String path) {
        return path.startsWith("/mypage") ||
               path.startsWith("/review") ||
               path.startsWith("/cart") ||
               path.startsWith("/order");
    }
    
    @Override
    public void destroy() {
        // 정리 작업
    }
}

