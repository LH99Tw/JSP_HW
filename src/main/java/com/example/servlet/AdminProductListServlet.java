package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.dao.SakeDAO;
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
 * 관리자 상품 목록 서블릿
 */
@WebServlet("/admin/product/list")
public class AdminProductListServlet extends HttpServlet {
    
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
            ProductDAO productDAO = new ProductDAO();
            
            // 파라미터 받기
            String region = request.getParameter("region");
            String style = request.getParameter("style");
            String search = request.getParameter("search");
            String published = request.getParameter("published");
            
            // 상품 목록 조회 (관리자는 모든 상품 조회)
            List<SakeProduct> products;
            if (region != null && !region.isEmpty()) {
                products = productDAO.findByRegion(region);
            } else if (style != null && !style.isEmpty()) {
                products = productDAO.findByStyle(style);
            } else if (search != null && !search.isEmpty()) {
                products = productDAO.search(search);
            } else {
                products = productDAO.findAllWithSakeInfo();
            }
            
            // 노출 여부 필터링 (클라이언트 측에서 처리)
            if (published != null && !published.isEmpty()) {
                boolean isPublished = Boolean.parseBoolean(published);
                products.removeIf(p -> p.isPublished() != isPublished);
            }
            
            // request에 설정
            request.setAttribute("products", products);
            request.setAttribute("region", region);
            request.setAttribute("style", style);
            request.setAttribute("search", search);
            request.setAttribute("published", published);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/admin/product/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

