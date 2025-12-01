package com.example.servlet;

import com.example.dao.FileDAO;
import com.example.model.FileInfo;
import com.example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 파일 탐색기 서블릿 (VSCode 스타일)
 */
@WebServlet("/explorer")
public class ExplorerServlet extends HttpServlet {
    
    private FileDAO fileDAO;
    
    @Override
    public void init() throws ServletException {
        fileDAO = new FileDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // 사용자의 모든 파일 가져오기
            List<FileInfo> files = fileDAO.findByUserId(user.getUserId());
            
            request.setAttribute("files", files);
            request.getRequestDispatcher("/jsp/files/explorer.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "파일 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/files/explorer.jsp").forward(request, response);
        }
    }
}

