package com.example.util;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 업로드 유틸리티 클래스
 */
public class FileUploadUtil {
    
    private static final String UPLOAD_DIR = "/uploads/review/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_FILES = 5;
    
    /**
     * 파일 업로드 처리
     * 
     * @param parts 파일 Part 배열
     * @param realPath 웹앱 실제 경로
     * @return 업로드된 파일명 리스트
     * @throws IOException 파일 저장 실패 시
     */
    public static List<String> uploadFiles(Part[] parts, String realPath) throws IOException {
        List<String> uploadedFiles = new ArrayList<>();
        
        if (parts == null || parts.length == 0) {
            return uploadedFiles;
        }
        
        // 업로드 디렉토리 생성
        String uploadPath = realPath + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        int fileCount = 0;
        for (Part part : parts) {
            if (part == null || part.getSubmittedFileName() == null || 
                part.getSubmittedFileName().isEmpty()) {
                continue;
            }
            
            if (fileCount >= MAX_FILES) {
                break;
            }
            
            // 파일 크기 체크
            if (part.getSize() > MAX_FILE_SIZE) {
                continue; // 크기 초과 파일은 건너뛰기
            }
            
            // 파일 확장자 체크
            String fileName = part.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!isValidImageExtension(extension)) {
                continue; // 이미지가 아니면 건너뛰기
            }
            
            // UUID로 파일명 생성
            String storedFileName = UUID.randomUUID().toString() + extension;
            String filePath = uploadPath + storedFileName;
            
            // 파일 저장
            try (InputStream input = part.getInputStream()) {
                Files.copy(input, new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.add(UPLOAD_DIR + storedFileName);
                fileCount++;
            }
        }
        
        return uploadedFiles;
    }
    
    /**
     * 이미지 확장자 검증
     */
    private static boolean isValidImageExtension(String extension) {
        String lowerExt = extension.toLowerCase();
        return lowerExt.equals(".jpg") || lowerExt.equals(".jpeg") || 
               lowerExt.equals(".png") || lowerExt.equals(".gif");
    }
    
    /**
     * 파일 삭제
     */
    public static boolean deleteFile(String filePath, String realPath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        File file = new File(realPath + filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }
}

