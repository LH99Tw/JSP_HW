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
 * 게시글 작성 서블릿
 */
@WebServlet("/board/write")
public class BoardWriteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 작성 폼 표시
        request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
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
        
        // 파라미터 받기
        String category = request.getParameter("category");
        String prefix = request.getParameter("prefix");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        // 유효성 검사
        if (title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "제목과 내용을 입력해주세요.");
            request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
            return;
        }
        
        if (title.length() > 200) {
            request.setAttribute("error", "제목은 200자 이하여야 합니다.");
            request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
            return;
        }
        
        try {
            PostDAO postDAO = new PostDAO();
            // 게시글 생성
            Post post = new Post();
            post.setUserId(user.getUserId());
            post.setCategory(category != null ? category : "일반");
            post.setPrefix(prefix != null && !prefix.isEmpty() ? prefix : null);
            post.setTitle(title.trim());
            post.setContent(content.trim());
            post.setNotice(false);
            post.setPinned(false);
            
            int postId = postDAO.insert(post);
            
            if (postId > 0) {
                response.sendRedirect(request.getContextPath() + "/board/detail?postId=" + postId);
            } else {
                request.setAttribute("error", "게시글 작성에 실패했습니다.");
                request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "게시글 작성 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/board/write.jsp").forward(request, response);
        }
    }
}

