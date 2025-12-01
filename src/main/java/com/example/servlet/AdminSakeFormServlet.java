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

/**
 * 관리자 사케 등록/수정 폼 서블릿
 */
@WebServlet("/admin/sake/form")
public class AdminSakeFormServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String sakeIdParam = request.getParameter("sakeId");
        
        if (sakeIdParam != null && !sakeIdParam.isEmpty()) {
            // 수정 모드
            try {
                int sakeId = Integer.parseInt(sakeIdParam);
                SakeDAO sakeDAO = new SakeDAO();
                Sake sake = sakeDAO.findById(sakeId);
                
                if (sake == null) {
                    request.setAttribute("error", "사케를 찾을 수 없습니다.");
                    response.sendRedirect(request.getContextPath() + "/admin/sake/list");
                    return;
                }
                
                request.setAttribute("sake", sake);
                request.setAttribute("isEdit", true);
            } catch (NumberFormatException | SQLException e) {
                response.sendRedirect(request.getContextPath() + "/admin/sake/list");
                return;
            }
        } else {
            // 등록 모드
            request.setAttribute("isEdit", false);
        }
        
        request.getRequestDispatcher("/jsp/admin/sake/form.jsp").forward(request, response);
    }
}

