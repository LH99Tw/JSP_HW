package com.example.servlet;

import com.example.dao.PostDAO;
import com.example.dao.PostLikeDAO;
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
 * 게시글 추천 서블릿
 */
@WebServlet("/board/like")
public class BoardLikeServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"로그인이 필요합니다.\"}");
            return;
        }
        
        String postIdParam = request.getParameter("postId");
        if (postIdParam == null || postIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"잘못된 요청입니다.\"}");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdParam);
            
            PostDAO postDAO = new PostDAO();
            PostLikeDAO postLikeDAO = new PostLikeDAO();
            
            // 이미 추천했는지 확인
            boolean isLiked = postLikeDAO.exists(postId, user.getUserId());
            
            if (isLiked) {
                // 추천 취소
                postLikeDAO.delete(postId, user.getUserId());
                postDAO.decrementLikeCount(postId);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"liked\": false}");
            } else {
                // 추천 추가
                postLikeDAO.insert(postId, user.getUserId());
                postDAO.incrementLikeCount(postId);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"liked\": true}");
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"오류가 발생했습니다.\"}");
        }
    }
}

