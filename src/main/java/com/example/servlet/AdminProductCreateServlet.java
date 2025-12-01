package com.example.servlet;

import com.example.dao.ProductDAO;
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

/**
 * 관리자 상품 등록 서블릿
 */
@WebServlet("/admin/product/create")
public class AdminProductCreateServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // 파라미터 받기
        String sakeIdParam = request.getParameter("sakeId");
        String priceParam = request.getParameter("price");
        String stockParam = request.getParameter("stock");
        String isPublishedParam = request.getParameter("isPublished");
        String label = request.getParameter("label");
        
        // 서버 측 유효성 검사
        if (sakeIdParam == null || sakeIdParam.isEmpty() ||
            priceParam == null || priceParam.isEmpty() ||
            stockParam == null || stockParam.isEmpty()) {
            request.setAttribute("error", "사케, 가격, 재고는 필수입니다.");
            response.sendRedirect(request.getContextPath() + "/admin/product/form");
            return;
        }
        
        try {
            int sakeId = Integer.parseInt(sakeIdParam);
            int price = Integer.parseInt(priceParam);
            int stock = Integer.parseInt(stockParam);
            boolean isPublished = isPublishedParam != null && "on".equals(isPublishedParam);
            
            if (price < 0 || stock < 0) {
                request.setAttribute("error", "가격과 재고는 0 이상이어야 합니다.");
                response.sendRedirect(request.getContextPath() + "/admin/product/form");
                return;
            }
            
            SakeProduct product = new SakeProduct();
            product.setSakeId(sakeId);
            product.setPrice(price);
            product.setStock(stock);
            product.setPublished(isPublished);
            product.setLabel(label != null ? label.trim() : null);
            
            ProductDAO productDAO = new ProductDAO();
            int productId = productDAO.insert(product);
            
            if (productId > 0) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
            } else {
                request.setAttribute("error", "상품 등록에 실패했습니다.");
                response.sendRedirect(request.getContextPath() + "/admin/product/form");
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 등록 중 오류가 발생했습니다: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/product/form");
        }
    }
}

