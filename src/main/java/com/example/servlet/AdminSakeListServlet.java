package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.Sake;
import com.example.model.User;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 관리자 사케 목록 서블릿
 */
@WebServlet("/admin/sake/list")
public class AdminSakeListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            SakeDAO sakeDAO = new SakeDAO();
            
            // 파라미터 받기
            String region = request.getParameter("region");
            String style = request.getParameter("style");
            String search = request.getParameter("search");
            
            // 사케 목록 조회
            List<Sake> sakes;
            if (region != null && !region.isEmpty()) {
                sakes = sakeDAO.findByRegion(region);
            } else if (style != null && !style.isEmpty()) {
                sakes = sakeDAO.findByStyle(style);
            } else if (search != null && !search.isEmpty()) {
                sakes = sakeDAO.search(search);
            } else {
                sakes = sakeDAO.findAll();
            }
            
            // request에 설정
            request.setAttribute("sakes", sakes);
            request.setAttribute("region", region);
            request.setAttribute("style", style);
            request.setAttribute("search", search);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/sake/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "사케 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

