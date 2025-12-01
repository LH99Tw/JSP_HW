package com.example.servlet;

import com.example.dao.ProductDAO;
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
 * 관리자 상품 삭제 서블릿
 */
@WebServlet("/admin/product/delete")
public class AdminProductDeleteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String productIdParam = request.getParameter("productId");
        
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.delete(productId);
            
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
        }
    }
}

