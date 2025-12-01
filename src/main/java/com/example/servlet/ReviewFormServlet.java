package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.Sake;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 리뷰 작성 폼 서블릿
 */
@WebServlet("/review/form")
public class ReviewFormServlet extends HttpServlet {
    
    private SakeDAO sakeDAO;
    
    @Override
    public void init() throws ServletException {
        sakeDAO = new SakeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sakeIdParam = request.getParameter("sakeId");
        String reviewIdParam = request.getParameter("reviewId"); // 수정 모드
        
        if (sakeIdParam == null || sakeIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
            return;
        }
        
        try {
            int sakeId = Integer.parseInt(sakeIdParam);
            Sake sake = sakeDAO.findById(sakeId);
            
            if (sake == null) {
                request.setAttribute("error", "사케를 찾을 수 없습니다.");
                request.getRequestDispatcher("/jsp/sake/list.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("sake", sake);
            request.setAttribute("isEdit", reviewIdParam != null && !reviewIdParam.isEmpty());
            
            // 수정 모드인 경우 리뷰 정보도 가져오기 (나중에 구현)
            if (reviewIdParam != null && !reviewIdParam.isEmpty()) {
                // ReviewDAO로 리뷰 정보 조회
            }
            
            request.getRequestDispatcher("/jsp/review/form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/sake/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/review/form.jsp").forward(request, response);
        }
    }
}

