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
import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 조회 서블릿
 */
@WebServlet("/cart")
public class CartViewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            CartDAO cartDAO = new CartDAO();
            ProductDAO productDAO = new ProductDAO();
            
            // 장바구니 목록 조회
            List<CartItem> cartItems = cartDAO.findByUserId(user.getUserId());
            
            // 각 장바구니 아이템에 상품 정보 추가
            List<CartItemWithProduct> cartItemsWithProduct = new ArrayList<>();
            int totalAmount = 0;
            
            for (CartItem item : cartItems) {
                com.example.model.SakeProduct product = productDAO.findById(item.getProductId());
                if (product != null) {
                    CartItemWithProduct itemWithProduct = new CartItemWithProduct();
                    itemWithProduct.setCartItem(item);
                    itemWithProduct.setProduct(product);
                    cartItemsWithProduct.add(itemWithProduct);
                    totalAmount += product.getPrice() * item.getQuantity();
                }
            }
            
            // request에 설정
            request.setAttribute("cartItems", cartItemsWithProduct);
            request.setAttribute("totalAmount", totalAmount);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/cart/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "장바구니를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
    
    /**
     * 장바구니 아이템과 상품 정보를 함께 담는 클래스
     */
    public static class CartItemWithProduct {
        private CartItem cartItem;
        private com.example.model.SakeProduct product;
        
        public CartItem getCartItem() {
            return cartItem;
        }
        
        public void setCartItem(CartItem cartItem) {
            this.cartItem = cartItem;
        }
        
        public com.example.model.SakeProduct getProduct() {
            return product;
        }
        
        public void setProduct(com.example.model.SakeProduct product) {
            this.product = product;
        }
    }
}

