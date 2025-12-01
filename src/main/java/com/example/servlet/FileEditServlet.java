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
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * 파일 편집 서블릿
 */
@WebServlet("/edit")
public class FileEditServlet extends HttpServlet {
    
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
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
            return;
        }
        
        try {
            int uploadId = Integer.parseInt(idParam);
            FileInfo fileInfo = fileDAO.findById(uploadId);
            
            if (fileInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
                return;
            }
            
            // 권한 체크
            if (fileInfo.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "편집 권한이 없습니다.");
                return;
            }
            
            // 텍스트 파일만 편집 가능
            if (!isTextFile(fileInfo.getContentType(), fileInfo.getOriginalFilename())) {
                request.setAttribute("error", "텍스트 파일만 편집할 수 있습니다.");
                request.setAttribute("fileInfo", fileInfo);
                request.getRequestDispatcher("/jsp/files/edit.jsp").forward(request, response);
                return;
            }
            
            // 파일 내용 읽기
            String content = readFileContent(fileInfo.getFilePath());
            
            request.setAttribute("fileInfo", fileInfo);
            request.setAttribute("content", content);
            request.getRequestDispatcher("/jsp/files/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID입니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 로그인 체크
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String idParam = request.getParameter("id");
        String content = request.getParameter("content");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일 ID가 필요합니다.");
            return;
        }
        
        try {
            int uploadId = Integer.parseInt(idParam);
            FileInfo fileInfo = fileDAO.findById(uploadId);
            
            if (fileInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
                return;
            }
            
            // 권한 체크
            if (fileInfo.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "편집 권한이 없습니다.");
                return;
            }
            
            // 파일 저장
            saveFileContent(fileInfo.getFilePath(), content != null ? content : "");
            
            // 파일 크기 업데이트
            File file = new File(fileInfo.getFilePath());
            fileInfo.setFileSize(file.length());
            
            response.sendRedirect(request.getContextPath() + "/files?success=saved");
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID입니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 텍스트 파일인지 확인
     */
    private boolean isTextFile(String contentType, String filename) {
        if (contentType != null && contentType.startsWith("text/")) {
            return true;
        }
        
        if (filename != null) {
            String lowerName = filename.toLowerCase();
            return lowerName.endsWith(".txt") || 
                   lowerName.endsWith(".java") || 
                   lowerName.endsWith(".jsp") || 
                   lowerName.endsWith(".js") || 
                   lowerName.endsWith(".css") || 
                   lowerName.endsWith(".html") || 
                   lowerName.endsWith(".xml") || 
                   lowerName.endsWith(".json") || 
                   lowerName.endsWith(".md") ||
                   lowerName.endsWith(".properties");
        }
        
        return false;
    }
    
    /**
     * 파일 내용 읽기
     */
    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * 파일 내용 저장
     */
    private void saveFileContent(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
}

