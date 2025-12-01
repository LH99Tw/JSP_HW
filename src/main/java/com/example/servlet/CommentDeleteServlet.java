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
 * 댓글 삭제 서블릿
 */
@WebServlet("/comment/delete")
public class CommentDeleteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        String commentIdParam = request.getParameter("commentId");
        String postIdParam = request.getParameter("postId");
        
        if (commentIdParam == null || commentIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int commentId = Integer.parseInt(commentIdParam);
            
            CommentDAO commentDAO = new CommentDAO();
            PostDAO postDAO = new PostDAO();
            
            // 작성자 본인 또는 관리자만 삭제 가능
            boolean success = commentDAO.delete(commentId, user.getUserId());
            
            if (success) {
                // 게시글의 댓글 수 업데이트
                if (postIdParam != null && !postIdParam.isEmpty()) {
                    int postId = Integer.parseInt(postIdParam);
                    postDAO.updateCommentCount(postId);
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

