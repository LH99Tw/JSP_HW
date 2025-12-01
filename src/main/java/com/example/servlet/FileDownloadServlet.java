package com.example.servlet;

import com.example.dao.FileDAO;
import com.example.model.FileInfo;
import com.example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

/**
 * 파일 다운로드 서블릿
 */
@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {
    
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
        
        String uploadIdParam = request.getParameter("id");
        if (uploadIdParam == null || uploadIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
            return;
        }
        
        try {
            int uploadId = Integer.parseInt(uploadIdParam);
            FileInfo fileInfo = fileDAO.findById(uploadId);
            
            if (fileInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
                return;
            }
            
            // 권한 체크 (본인 파일만 다운로드 가능)
            if (fileInfo.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "다운로드 권한이 없습니다.");
                return;
            }
            
            // 파일 읽기
            File file = new File(fileInfo.getFilePath());
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일이 존재하지 않습니다.");
                return;
            }
            
            // 응답 헤더 설정
            response.setContentType(fileInfo.getContentType() != null ? fileInfo.getContentType() : "application/octet-stream");
            response.setContentLengthLong(fileInfo.getFileSize());
            
            // 파일명 인코딩 (한글 파일명 지원)
            String encodedFilename = new String(
                fileInfo.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1"
            );
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"" + encodedFilename + "\"");
            
            // 파일 전송
            try (InputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID입니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");
        }
    }
}

