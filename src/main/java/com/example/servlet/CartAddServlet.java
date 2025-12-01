package com.example.servlet;

import com.example.dao.CartDAO;
import com.example.dao.ProductDAO;
import com.example.model.CartItem;
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
 * 장바구니 추가 서블릿
 */
@WebServlet("/cart/add")
public class CartAddServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");
        
        if (productIdParam == null || quantityParam == null) {
            response.sendRedirect(request.getContextPath() + "/product/list");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            
            if (quantity <= 0) {
                request.setAttribute("error", "수량은 1개 이상이어야 합니다.");
                response.sendRedirect(request.getContextPath() + "/product/detail?productId=" + productId);
                return;
            }
            
            ProductDAO productDAO = new ProductDAO();
            CartDAO cartDAO = new CartDAO();
            
            // 상품 재고 확인
            com.example.model.SakeProduct product = productDAO.findById(productId);
            if (product == null) {
                request.setAttribute("error", "상품을 찾을 수 없습니다.");
                response.sendRedirect(request.getContextPath() + "/product/list");
                return;
            }
            
            if (product.getStock() < quantity) {
                request.setAttribute("error", "재고가 부족합니다. (재고: " + product.getStock() + "개)");
                response.sendRedirect(request.getContextPath() + "/product/detail?productId=" + productId);
                return;
            }
            
            // 장바구니에 추가
            CartItem cartItem = new CartItem();
            cartItem.setUserId(user.getUserId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            
            boolean success = cartDAO.insert(cartItem);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart");
            } else {
                request.setAttribute("error", "장바구니 추가에 실패했습니다.");
                response.sendRedirect(request.getContextPath() + "/product/detail?productId=" + productId);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product/list");
        }
    }
}

