package com.example.servlet;

import com.example.dao.CartDAO;
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
import java.util.List;

/**
 * 주문 생성 서블릿
 */
@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 배송 정보 파라미터 받기
        String recipientName = request.getParameter("recipientName");
        String recipientPhone = request.getParameter("recipientPhone");
        String shippingAddress = request.getParameter("shippingAddress");
        String shippingAddressDetail = request.getParameter("shippingAddressDetail");
        String shippingPostcode = request.getParameter("shippingPostcode");
        String memo = request.getParameter("memo");
        
        // 서버 측 유효성 검사
        if (recipientName == null || recipientName.trim().isEmpty() ||
            recipientPhone == null || recipientPhone.trim().isEmpty() ||
            shippingAddress == null || shippingAddress.trim().isEmpty()) {
            request.setAttribute("error", "배송 정보를 모두 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/order/form");
            return;
        }
        
        try {
            CartDAO cartDAO = new CartDAO();
            ProductDAO productDAO = new ProductDAO();
            OrderDAO orderDAO = new OrderDAO();
            
            // 장바구니 목록 조회
            List<com.example.model.CartItem> cartItems = cartDAO.findByUserId(user.getUserId());
            
            if (cartItems.isEmpty()) {
                request.setAttribute("error", "장바구니가 비어있습니다.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            
            // 재고 확인 및 총 금액 계산
            int totalAmount = 0;
            for (com.example.model.CartItem item : cartItems) {
                com.example.model.SakeProduct product = productDAO.findById(item.getProductId());
                if (product == null) {
                    request.setAttribute("error", "존재하지 않는 상품이 있습니다.");
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
                if (product.getStock() < item.getQuantity()) {
                    request.setAttribute("error", "재고가 부족한 상품이 있습니다: " + product.getSakeName());
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
                totalAmount += product.getPrice() * item.getQuantity();
            }
            
            // 주문 생성 (트랜잭션 처리)
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setRecipientName(recipientName.trim());
            order.setRecipientPhone(recipientPhone.trim());
            order.setShippingAddress(shippingAddress.trim());
            order.setShippingAddressDetail(shippingAddressDetail != null ? shippingAddressDetail.trim() : null);
            order.setShippingPostcode(shippingPostcode != null ? shippingPostcode.trim() : null);
            order.setMemo(memo != null ? memo.trim() : null);
            order.setTotalAmount(totalAmount);
            order.setStatus("PENDING"); // 주문 대기 상태
            
            int orderId = orderDAO.insert(order);
            
            if (orderId > 0) {
                // 주문 상품 추가 및 재고 차감
                for (com.example.model.CartItem item : cartItems) {
                    com.example.model.SakeProduct product = productDAO.findById(item.getProductId());
                    
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrderId(orderId);
                    orderItem.setProductId(item.getProductId());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(product.getPrice());
                    
                    orderDAO.insertOrderItem(orderItem);
                    
                    // 재고 차감
                    productDAO.updateStock(item.getProductId(), -item.getQuantity());
                }
                
                // 장바구니 비우기
                cartDAO.clearCart(user.getUserId());
                
                // 주문 완료 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/order/complete?orderId=" + orderId);
            } else {
                request.setAttribute("error", "주문 생성에 실패했습니다.");
                response.sendRedirect(request.getContextPath() + "/order/form");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "주문 처리 중 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/order/form");
        }
    }
}

