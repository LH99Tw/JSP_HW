package com.example.filter;

import com.example.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 관리자 권한 필터
 * 관리자 페이지 접근 시 관리자 권한 확인
 */
public class AdminAuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 작업
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        
        // 로그인하지 않았거나 관리자가 아닌 경우
        if (!SessionUtil.isAdmin(session)) {
            // 로그인 페이지로 리다이렉트
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/login?error=admin_required");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // 정리 작업
    }
}

