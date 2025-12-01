package com.example.servlet;

import com.example.dao.PostDAO;
import com.example.model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 게시판 목록 서블릿
 */
@WebServlet("/board/list")
public class BoardListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 파라미터 받기
            String sortBy = request.getParameter("sortBy"); // 전체글, 인기글, 조회순
            String category = request.getParameter("category"); // 전체글, 공지, 일반, 질문 등
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");
            
            // 기본값 설정
            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "전체글";
            }
            if (category == null || category.isEmpty()) {
                category = "전체글";
            }
            
            int page = 1;
            int size = 50; // 디시인사이드 스타일: 기본 50개
            
            try {
                page = pageParam != null ? Integer.parseInt(pageParam) : 1;
                size = sizeParam != null ? Integer.parseInt(sizeParam) : 50;
            } catch (NumberFormatException e) {
                // 기본값 사용
            }
            
            // DAO 생성 및 게시글 목록 조회
            PostDAO postDAO = new PostDAO();
            List<Post> posts = postDAO.findAll(sortBy, category, page, size);
            
            // 전체 게시글 수
            int totalCount = postDAO.getTotalCount(category);
            int totalPages = (int) Math.ceil((double) totalCount / size);
            
            // request에 설정
            request.setAttribute("posts", posts);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("category", category);
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPages", totalPages);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/board/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "게시글 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<h1>오류 발생</h1><p>" + e.getMessage() + "</p>");
        }
    }
}
