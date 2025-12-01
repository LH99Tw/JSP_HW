package com.example.servlet;

import com.example.dao.CommentDAO;
import com.example.dao.PostDAO;
import com.example.dao.PostLikeDAO;
import com.example.model.Post;
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
import java.util.List;

/**
 * 게시글 상세 서블릿
 */
@WebServlet("/board/detail")
public class BoardDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String postIdParam = request.getParameter("postId");
        
        if (postIdParam == null || postIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdParam);
            
            PostDAO postDAO = new PostDAO();
            CommentDAO commentDAO = new CommentDAO();
            PostLikeDAO postLikeDAO = new PostLikeDAO();
            
            // 게시글 조회
            Post post = postDAO.findById(postId);
            
            if (post == null) {
                request.setAttribute("error", "게시글을 찾을 수 없습니다.");
                request.getRequestDispatcher("/jsp/error/404.jsp").forward(request, response);
                return;
            }
            
            // 조회수 증가
            postDAO.incrementViewCount(postId);
            post.setViewCount(post.getViewCount() + 1);
            
            // 댓글 목록 조회
            List<PostComment> comments = commentDAO.findByPostId(postId);
            
            // 현재 사용자가 추천했는지 확인
            User user = SessionUtil.getUser(request.getSession(false));
            boolean isLiked = false;
            if (user != null) {
                isLiked = postLikeDAO.exists(postId, user.getUserId());
            }
            
            // request에 설정
            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.setAttribute("isLiked", isLiked);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/board/detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "게시글을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/error/error.jsp").forward(request, response);
        }
    }
}

