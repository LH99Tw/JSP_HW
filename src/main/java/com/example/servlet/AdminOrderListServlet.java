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
import java.util.List;

/**
 * 관리자 주문 목록 서블릿
 */
@WebServlet("/admin/order/list")
public class AdminOrderListServlet extends HttpServlet {
    
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
            OrderDAO orderDAO = new OrderDAO();
            
            // 파라미터 받기
            String status = request.getParameter("status");
            String userIdParam = request.getParameter("userId");
            
            // 주문 목록 조회
            List<Order> orders;
            if (userIdParam != null && !userIdParam.isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                orders = orderDAO.findByUserId(userId);
            } else {
                orders = orderDAO.findAll();
            }
            
            // 상태 필터링
            if (status != null && !status.isEmpty()) {
                orders.removeIf(o -> !status.equals(o.getStatus()));
            }
            
            // request에 설정
            request.setAttribute("orders", orders);
            request.setAttribute("status", status);
            request.setAttribute("userId", userIdParam);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/order/list.jsp").forward(request, response);
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "주문 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

