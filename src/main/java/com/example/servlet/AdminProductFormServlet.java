package com.example.servlet;

import com.example.dao.ProductDAO;
import com.example.dao.SakeDAO;
import com.example.model.Sake;
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
 * 관리자 상품 등록/수정 폼 서블릿
 */
@WebServlet("/admin/product/form")
public class AdminProductFormServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String productIdParam = request.getParameter("productId");
        
        if (productIdParam != null && !productIdParam.isEmpty()) {
            // 수정 모드
            try {
                int productId = Integer.parseInt(productIdParam);
                ProductDAO productDAO = new ProductDAO();
                SakeProduct product = productDAO.findByIdWithSakeInfo(productId);
                
                if (product == null) {
                    request.setAttribute("error", "상품을 찾을 수 없습니다.");
                    response.sendRedirect(request.getContextPath() + "/admin/product/list");
                    return;
                }
                
                request.setAttribute("product", product);
                request.setAttribute("isEdit", true);
            } catch (NumberFormatException | SQLException e) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
                return;
            }
        } else {
            // 등록 모드
            request.setAttribute("isEdit", false);
        }
        
        // 사케 목록 조회 (드롭다운용)
        try {
            SakeDAO sakeDAO = new SakeDAO();
            List<Sake> sakes = sakeDAO.findAll();
            request.setAttribute("sakes", sakes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        request.getRequestDispatcher("/jsp/admin/product/form.jsp").forward(request, response);
    }
}

