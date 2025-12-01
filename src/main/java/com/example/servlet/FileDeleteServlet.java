package com.example.servlet;

import com.example.dao.FileDAO;
import com.example.model.FileInfo;
import com.example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 파일 삭제 서블릿
 */
@WebServlet("/delete")
public class FileDeleteServlet extends HttpServlet {
    
    private FileDAO fileDAO;
    
    @Override
    public void init() throws ServletException {
        fileDAO = new FileDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 로그인 체크
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String uploadIdParam = request.getParameter("id");
        if (uploadIdParam == null || uploadIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
            return;
        }
        
        try {
            int uploadId = Integer.parseInt(uploadIdParam);
            
            // 파일 정보 가져오기
            FileInfo fileInfo = fileDAO.findById(uploadId);
            if (fileInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
                return;
            }
            
            // 권한 체크
            if (fileInfo.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "삭제 권한이 없습니다.");
                return;
            }
            
            // 파일 삭제
            File file = new File(fileInfo.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            
            // 데이터베이스에서 삭제
            boolean deleted = fileDAO.deleteByUser(uploadId, user.getUserId());
            
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/files?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/files?error=delete_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID입니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");
        }
    }
}

