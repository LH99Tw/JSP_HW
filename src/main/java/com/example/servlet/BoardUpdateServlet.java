package com.example.servlet;

import com.example.dao.PostDAO;
import com.example.model.Post;
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
 * 게시글 수정 서블릿
 */
@WebServlet("/board/update")
public class BoardUpdateServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String postIdParam = request.getParameter("postId");
        if (postIdParam == null || postIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdParam);
            PostDAO postDAO = new PostDAO();
            Post post = postDAO.findById(postId);
            
            if (post == null) {
                response.sendRedirect(request.getContextPath() + "/board/list");
                return;
            }
            
            // 작성자 본인만 수정 가능
            if (post.getUserId() != user.getUserId() && !"ADMIN".equals(user.getRole())) {
                request.setAttribute("error", "수정 권한이 없습니다.");
                request.getRequestDispatcher("/jsp/board/detail?postId=" + postId).forward(request, response);
                return;
            }
            
            request.setAttribute("post", post);
            request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
            
        } catch (NumberFormatException | SQLException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String postIdParam = request.getParameter("postId");
        String category = request.getParameter("category");
        String prefix = request.getParameter("prefix");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        if (postIdParam == null || title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdParam);
            PostDAO postDAO = new PostDAO();
            Post post = postDAO.findById(postId);
            
            if (post == null) {
                response.sendRedirect(request.getContextPath() + "/board/list");
                return;
            }
            
            // 작성자 본인만 수정 가능
            if (post.getUserId() != user.getUserId() && !"ADMIN".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
                return;
            }
            
            // 게시글 수정
            post.setCategory(category != null ? category : "일반");
            post.setPrefix(prefix != null && !prefix.isEmpty() ? prefix : null);
            post.setTitle(title.trim());
            post.setContent(content.trim());
            
            boolean success = postDAO.update(post);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
            } else {
                request.setAttribute("error", "게시글 수정에 실패했습니다.");
                request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
}

