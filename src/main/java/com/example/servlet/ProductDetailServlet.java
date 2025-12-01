package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.dao.SakeDAO;
import com.example.model.SakeProduct;
import com.example.model.Sake;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 상품 상세 서블릿
 */
@WebServlet("/product/detail")
public class ProductDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productIdParam = request.getParameter("productId");
        
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product/list");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            
            ProductDAO productDAO = new ProductDAO();
            SakeDAO sakeDAO = new SakeDAO();
            
            // 상품 조회
            SakeProduct product = productDAO.findById(productId);
            
            if (product == null) {
                request.setAttribute("error", "상품을 찾을 수 없습니다.");
                request.getRequestDispatcher("/jsp/error/404.jsp").forward(request, response);
                return;
            }
            
            // 사케 정보 조회
            Sake sake = sakeDAO.findById(product.getSakeId());
            
            // request에 설정
            request.setAttribute("product", product);
            request.setAttribute("sake", sake);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/product/detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 정보를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

