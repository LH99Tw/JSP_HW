package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.User;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 관리자 사케 삭제 서블릿
 */
@WebServlet("/admin/sake/delete")
public class AdminSakeDeleteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String sakeIdParam = request.getParameter("sakeId");
        
        if (sakeIdParam == null || sakeIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/sake/list");
            return;
        }
        
        try {
            int sakeId = Integer.parseInt(sakeIdParam);
            SakeDAO sakeDAO = new SakeDAO();
            boolean success = sakeDAO.delete(sakeId);
            
            response.sendRedirect(request.getContextPath() + "/admin/sake/list");
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/sake/list");
        }
    }
}

