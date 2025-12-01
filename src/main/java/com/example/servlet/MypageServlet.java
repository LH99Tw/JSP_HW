package com.example.servlet;

import com.example.dao.OrderDAO;
import com.example.dao.ProductDAO;
import com.example.dao.ReviewDAO;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.SakeProduct;
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
 * 마이페이지 서블릿
 */
@WebServlet("/mypage")
public class MypageServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크 (필터에서 처리되지만 추가 안전장치)
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            ReviewDAO reviewDAO = new ReviewDAO();
            OrderDAO orderDAO = new OrderDAO();
            ProductDAO productDAO = new ProductDAO();
            
            // 사용자 리뷰 목록 조회
            request.setAttribute("reviews", reviewDAO.findByUserId(user.getUserId()));
            
            // 사용자 주문 목록 조회
            List<Order> orders = orderDAO.findByUserId(user.getUserId());
            
            // 주문 상품 정보 조회
            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    SakeProduct product = productDAO.findByIdWithSakeInfo(item.getProductId());
                    if (product != null) {
                        item.setProduct(product);
                    }
                }
            }
            
            request.setAttribute("orders", orders);
            
            // 마이페이지로 포워드
            request.getRequestDispatcher("/jsp/mypage/index.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/mypage/index.jsp").forward(request, response);
        }
    }
}

