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
 * 파일 뷰어 서블릿 (텍스트 파일 및 PDF 미리보기)
 */
@WebServlet("/view")
public class FileViewServlet extends HttpServlet {
    
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
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
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
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "파일을 볼 권한이 없습니다.");
                return;
            }
            
            File file = new File(fileInfo.getFilePath());
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일이 존재하지 않습니다.");
                return;
            }
            
            String filename = fileInfo.getOriginalFilename().toLowerCase();
            String contentType = fileInfo.getContentType();
            
            // 파일 타입에 따라 처리
            if (filename.endsWith(".pdf")) {
                // PDF 파일 - 직접 스트리밍
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + 
                    new String(fileInfo.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1") + "\"");
                
                try (InputStream in = new FileInputStream(file);
                     OutputStream out = response.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                return;
            } else if (isTextFile(filename, contentType)) {
                // 텍스트 파일 - 내용을 읽어서 JSON으로 반환
                String content = readFileContent(file);
                
                response.setContentType("application/json; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                
                // JSON 응답 (escapeJson이 이미 따옴표를 포함하므로 직접 연결)
                String filenameEscaped = escapeJson(fileInfo.getOriginalFilename());
                String contentEscaped = escapeJson(content);
                String json = "{\"success\":true,\"filename\":" + filenameEscaped + ",\"content\":" + contentEscaped + ",\"type\":\"text\"}";
                
                response.getWriter().write(json);
                return;
            } else {
                // 지원하지 않는 파일 타입
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(
                    "{\"success\":false,\"message\":\"이 파일 타입은 미리보기를 지원하지 않습니다.\",\"type\":\"unsupported\"}"
                );
                return;
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 파일 ID입니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.");
        }
    }
    
    /**
     * 텍스트 파일인지 확인
     */
    private boolean isTextFile(String filename, String contentType) {
        if (contentType != null && contentType.startsWith("text/")) {
            return true;
        }
        
        return filename.endsWith(".txt") || 
               filename.endsWith(".md") || 
               filename.endsWith(".json") || 
               filename.endsWith(".java") || 
               filename.endsWith(".jsp") || 
               filename.endsWith(".js") || 
               filename.endsWith(".css") || 
               filename.endsWith(".html") || 
               filename.endsWith(".xml") || 
               filename.endsWith(".properties") ||
               filename.endsWith(".yml") ||
               filename.endsWith(".yaml");
    }
    
    /**
     * 파일 내용 읽기
     */
    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * JSON 이스케이프
     */
    private String escapeJson(String str) {
        if (str == null) {
            return "null";
        }
        return "\"" + str.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }
}

