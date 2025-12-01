package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ML 서비스 클라이언트 유틸리티 클래스
 */
public class MLClient {
    
    private static final String ML_SERVICE_URL = System.getenv("ML_SERVICE_URL") != null 
            ? System.getenv("ML_SERVICE_URL") 
            : "http://localhost:8000";
    
    private static final int CONNECT_TIMEOUT = 5000; // 5초
    private static final int READ_TIMEOUT = 10000; // 10초
    
    /**
     * 사케 추천 요청
     * @param userId 사용자 ID
     * @param preferences 선호 맛 정보 (JSON 문자열)
     * @return 추천 사케 ID 리스트
     */
    public static List<Integer> recommendSake(int userId, String preferences) {
        List<Integer> recommendations = new ArrayList<>();
        
        try {
            URL url = new URL(ML_SERVICE_URL + "/api/v1/recommend");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);
            
            // 요청 본문 생성 (간단한 JSON 생성)
            StringBuilder requestBody = new StringBuilder();
            requestBody.append("{\"user_id\":").append(userId);
            if (preferences != null && !preferences.isEmpty()) {
                requestBody.append(",\"preferences\":").append(preferences);
            }
            requestBody.append("}");
            
            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // JSON 파싱 (간단한 정규식 기반)
                    String jsonStr = response.toString();
                    // "sake_ids": [1, 2, 3] 형식에서 숫자 추출
                    Pattern pattern = Pattern.compile("\"sake_ids\"\\s*:\\s*\\[([^\\]]+)\\]");
                    Matcher matcher = pattern.matcher(jsonStr);
                    if (matcher.find()) {
                        String idsStr = matcher.group(1);
                        String[] ids = idsStr.split(",");
                        for (String id : ids) {
                            try {
                                recommendations.add(Integer.parseInt(id.trim()));
                            } catch (NumberFormatException e) {
                                // 무시
                            }
                        }
                    }
                }
            } else {
                System.err.println("ML 서비스 추천 요청 실패: " + responseCode);
            }
            
        } catch (Exception e) {
            System.err.println("ML 서비스 추천 요청 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recommendations;
    }
    
    /**
     * 리뷰 텍스트 분석 (맛 태그 추출)
     * @param reviewText 리뷰 텍스트
     * @return 맛 태그 리스트 (쉼표로 구분된 문자열)
     */
    public static String analyzeTaste(String reviewText) {
        if (reviewText == null || reviewText.trim().isEmpty()) {
            return "";
        }
        
        try {
            URL url = new URL(ML_SERVICE_URL + "/api/v1/analyze-taste");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);
            
            // 요청 본문 생성 (간단한 JSON 생성)
            // JSON 이스케이프 처리
            String escapedText = reviewText.replace("\\", "\\\\")
                                          .replace("\"", "\\\"")
                                          .replace("\n", "\\n")
                                          .replace("\r", "\\r")
                                          .replace("\t", "\\t");
            String requestBody = "{\"review_text\":\"" + escapedText + "\"}";
            
            // 요청 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // JSON 파싱 (간단한 정규식 기반)
                    String jsonStr = response.toString();
                    // "tags": ["tag1", "tag2"] 형식에서 태그 추출
                    Pattern pattern = Pattern.compile("\"tags\"\\s*:\\s*\\[([^\\]]+)\\]");
                    Matcher matcher = pattern.matcher(jsonStr);
                    if (matcher.find()) {
                        String tagsStr = matcher.group(1);
                        // 따옴표 제거 및 태그 추출
                        Pattern tagPattern = Pattern.compile("\"([^\"]+)\"");
                        Matcher tagMatcher = tagPattern.matcher(tagsStr);
                        List<String> tagList = new ArrayList<>();
                        while (tagMatcher.find()) {
                            tagList.add(tagMatcher.group(1));
                        }
                        return String.join(",", tagList);
                    }
                }
            } else {
                System.err.println("ML 서비스 맛 분석 요청 실패: " + responseCode);
            }
            
        } catch (Exception e) {
            System.err.println("ML 서비스 맛 분석 요청 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "";
    }
    
    /**
     * ML 서비스 헬스 체크
     * @return 서비스가 정상이면 true
     */
    public static boolean healthCheck() {
        try {
            URL url = new URL(ML_SERVICE_URL + "/health");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
            
        } catch (Exception e) {
            System.err.println("ML 서비스 헬스 체크 실패: " + e.getMessage());
            return false;
        }
    }
}

