package com.example.servlet;

import com.example.dao.OrderDAO;
import com.example.dao.ProductDAO;
import com.example.model.Order;
import com.example.model.OrderItem;
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
 * 관리자 주문 상세 서블릿
 */
@WebServlet("/admin/order/detail")
public class AdminOrderDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String orderIdParam = request.getParameter("orderId");
        
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/order/list");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdParam);
            OrderDAO orderDAO = new OrderDAO();
            ProductDAO productDAO = new ProductDAO();
            
            Order order = orderDAO.findById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "주문을 찾을 수 없습니다.");
                response.sendRedirect(request.getContextPath() + "/admin/order/list");
                return;
            }
            
            // 주문 상품에 상품 정보 추가
            for (OrderItem item : order.getItems()) {
                com.example.model.SakeProduct product = productDAO.findById(item.getProductId());
                if (product != null) {
                    item.setProduct(product);
                }
            }
            
            // request에 설정
            request.setAttribute("order", order);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/order/detail.jsp").forward(request, response);
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/order/list");
        }
    }
}

