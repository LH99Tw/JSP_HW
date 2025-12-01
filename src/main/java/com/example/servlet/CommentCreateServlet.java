package com.example.servlet;

import com.example.dao.CommentDAO;
import com.example.dao.PostDAO;
import com.example.model.PostComment;
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
 * 댓글 작성 서블릿
 */
@WebServlet("/comment/create")
public class CommentCreateServlet extends HttpServlet {
    
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
        String content = request.getParameter("content");
        
        if (postIdParam == null || content == null || content.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdParam);
            
            CommentDAO commentDAO = new CommentDAO();
            PostDAO postDAO = new PostDAO();
            
            // 댓글 생성
            PostComment comment = new PostComment();
            comment.setPostId(postId);
            comment.setUserId(user.getUserId());
            comment.setContent(content.trim());
            comment.setParentCommentId(null);
            
            int commentId = commentDAO.insert(comment);
            
            if (commentId > 0) {
                // 게시글의 댓글 수 업데이트
                postDAO.updateCommentCount(postId);
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
            } else {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
}

