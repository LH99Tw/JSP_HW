package com.example.servlet;

import com.example.dao.CartDAO;
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
 * 장바구니 삭제 서블릿
 */
@WebServlet("/cart/delete")
public class CartDeleteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String cartItemIdParam = request.getParameter("cartItemId");
        
        if (cartItemIdParam == null || cartItemIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        try {
            int cartItemId = Integer.parseInt(cartItemIdParam);
            
            CartDAO cartDAO = new CartDAO();
            boolean success = cartDAO.delete(cartItemId);
            
            response.sendRedirect(request.getContextPath() + "/cart");
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}

