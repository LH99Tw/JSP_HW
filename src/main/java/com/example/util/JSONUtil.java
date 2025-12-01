package com.example.util;

import com.example.model.FileInfo;
import java.util.List;

/**
 * JSON 유틸리티 클래스
 */
public class JSONUtil {
    
    /**
     * 파일 목록을 JSON 문자열로 변환
     */
    public static String filesToJson(List<FileInfo> files) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < files.size(); i++) {
            FileInfo file = files.get(i);
            if (i > 0) sb.append(",");
            sb.append("{");
            sb.append("\"uploadId\":").append(file.getUploadId()).append(",");
            sb.append("\"originalFilename\":\"").append(escapeJson(file.getOriginalFilename())).append("\",");
            sb.append("\"storedFilename\":\"").append(escapeJson(file.getStoredFilename())).append("\",");
            sb.append("\"fileSize\":").append(file.getFileSize()).append(",");
            sb.append("\"contentType\":\"").append(escapeJson(file.getContentType() != null ? file.getContentType() : "")).append("\",");
            sb.append("\"uploadedAt\":\"").append(file.getUploadedAt() != null ? file.getUploadedAt().toString() : "").append("\"");
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * JSON 문자열 이스케이프
     */
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * 성공 응답 JSON 생성
     */
    public static String successJson(String key, Object value) {
        return "{\"success\":true,\"" + key + "\":" + value + "}";
    }
    
    /**
     * 실패 응답 JSON 생성
     */
    public static String errorJson(String message) {
        return "{\"success\":false,\"message\":\"" + escapeJson(message) + "\"}";
    }
}

