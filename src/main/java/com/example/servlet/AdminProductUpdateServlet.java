package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.model.SakeProduct;
import com.example.model.User;
import com.example.util.FileUploadUtil;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 관리자 상품 수정 서블릿
 */
@WebServlet("/admin/product/update")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB
public class AdminProductUpdateServlet extends HttpServlet {
    
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
            response.sendRedirect(request.getContextPath() + "/admin/product/form?productId=" + productIdParam);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            int sakeId = Integer.parseInt(sakeIdParam);
            int price = Integer.parseInt(priceParam);
            int stock = Integer.parseInt(stockParam);
            boolean isPublished = isPublishedParam != null && "on".equals(isPublishedParam);
            
            if (price < 0 || stock < 0) {
                response.sendRedirect(request.getContextPath() + "/admin/product/form?productId=" + productId);
                return;
            }
            
            ProductDAO productDAO = new ProductDAO();
            SakeProduct product = productDAO.findById(productId);
            
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
                return;
            }
            
            // 이미지 업로드 처리 (새 이미지가 업로드된 경우에만)
            try {
                Part imagePart = request.getPart("image");
                if (imagePart != null && imagePart.getSize() > 0) {
                    String realPath = getServletContext().getRealPath("/");
                    // 기존 이미지 삭제
                    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                        FileUploadUtil.deleteFile(product.getImageUrl(), realPath);
                    }
                    // 새 이미지 업로드
                    String imageUrl = FileUploadUtil.uploadProductImage(imagePart, realPath);
                    product.setImageUrl(imageUrl);
                }
                // 이미지가 업로드되지 않았으면 기존 이미지 유지
            } catch (Exception e) {
                e.printStackTrace();
                // 이미지 업로드 실패해도 상품 정보는 업데이트 가능
            }
            
            // 상품 정보 업데이트
            product.setSakeId(sakeId);
            product.setPrice(price);
            product.setStock(stock);
            product.setPublished(isPublished);
            product.setLabel(label != null ? label.trim() : null);
            
            boolean success = productDAO.update(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/product/form?productId=" + productId);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
        }
    }
}

