package com.example.servlet;

import com.example.dao.FileDAO;
import com.example.model.FileInfo;
import com.example.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 파일 업로드 서블릿
 */
@WebServlet("/upload")
@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,      // 10MB
    maxRequestSize = 50 * 1024 * 1024,    // 50MB
    fileSizeThreshold = 1024 * 1024       // 1MB
)
public class FileUploadServlet extends HttpServlet {
    
    private FileDAO fileDAO;
    private static final String UPLOAD_DIR = "uploads";
    
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
        
        request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
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
        
        try {
            // 업로드 디렉토리 생성
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 파일 파트 가져오기
            Part filePart = request.getPart("file");
            
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "파일을 선택해주세요.");
                request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
                return;
            }
            
            // 파일 정보 추출
            String originalFilename = getFilename(filePart);
            String contentType = filePart.getContentType();
            long fileSize = filePart.getSize();
            
            // 파일 크기 검증
            if (fileSize > 10 * 1024 * 1024) {
                request.setAttribute("error", "파일 크기는 10MB를 초과할 수 없습니다.");
                request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
                return;
            }
            
            // 고유 파일명 생성
            String extension = "";
            int lastDot = originalFilename.lastIndexOf('.');
            if (lastDot > 0) {
                extension = originalFilename.substring(lastDot);
            }
            String storedFilename = UUID.randomUUID().toString() + extension;
            String filePath = uploadPath + File.separator + storedFilename;
            
            // 파일 저장
            filePart.write(filePath);
            
            // 데이터베이스에 파일 정보 저장
            FileInfo fileInfo = new FileInfo(
                user.getUserId(),
                originalFilename,
                storedFilename,
                filePath,
                fileSize,
                contentType
            );
            
            int uploadId = fileDAO.insert(fileInfo);
            
            if (uploadId > 0) {
                request.setAttribute("success", "파일이 성공적으로 업로드되었습니다.");
                request.setAttribute("fileInfo", fileInfo);
            } else {
                // 파일 저장 실패 시 업로드한 파일 삭제
                new File(filePath).delete();
                request.setAttribute("error", "파일 업로드에 실패했습니다.");
            }
            
            request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            request.getRequestDispatcher("/jsp/files/upload.jsp").forward(request, response);
        }
    }
    
    /**
     * Part에서 파일명 추출
     */
    private String getFilename(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "unknown";
    }
}

