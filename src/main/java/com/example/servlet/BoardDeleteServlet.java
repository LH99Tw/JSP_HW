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
 * 게시글 삭제 서블릿
 */
@WebServlet("/board/delete")
public class BoardDeleteServlet extends HttpServlet {
    
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
            
            // 작성자 본인 또는 관리자만 삭제 가능
            if (post.getUserId() != user.getUserId() && !"ADMIN".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
                return;
            }
            
            boolean success = postDAO.delete(postId, user.getUserId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/board/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
}

