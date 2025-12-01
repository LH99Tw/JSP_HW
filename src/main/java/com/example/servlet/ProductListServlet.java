package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.model.SakeProduct;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 상품 목록 서블릿
 */
@WebServlet("/product/list")
public class ProductListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("ProductListServlet: doGet called");
        try {
            ProductDAO productDAO = new ProductDAO();
            
            // 파라미터 받기
            String region = request.getParameter("region");
            String style = request.getParameter("style");
            String search = request.getParameter("search");
            
            // 상품 목록 조회
            List<SakeProduct> products;
            if (region != null && !region.isEmpty()) {
                products = productDAO.findByRegion(region);
            } else if (style != null && !style.isEmpty()) {
                products = productDAO.findByStyle(style);
            } else if (search != null && !search.isEmpty()) {
                products = productDAO.search(search);
            } else {
                products = productDAO.findAll();
            }
            
            // request에 설정
            request.setAttribute("products", products);
            request.setAttribute("region", region);
            request.setAttribute("style", style);
            request.setAttribute("search", search);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/product/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

