package com.example.servlet;

import com.example.dao.OrderDAO;
import com.example.model.Order;
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
 * 주문 완료 서블릿
 */
@WebServlet("/order/complete")
public class OrderCompleteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String orderIdParam = request.getParameter("orderId");
        
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdParam);
            
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.findById(orderId);
            
            if (order == null || order.getUserId() != user.getUserId()) {
                request.setAttribute("error", "주문을 찾을 수 없습니다.");
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            // request에 설정
            request.setAttribute("order", order);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/order/complete.jsp").forward(request, response);
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}

