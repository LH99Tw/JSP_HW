package com.example.servlet;

import com.example.dao.ReviewDAO;
import com.example.dao.UploadDAO;
import com.example.model.SakeReview;
import com.example.model.User;
import com.example.util.FileUploadUtil;
import com.example.util.MLClient;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 리뷰 작성 서블릿
 */
@WebServlet("/review/create")
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,      // 5MB
    maxRequestSize = 25 * 1024 * 1024,  // 25MB (5개 파일 * 5MB)
    fileSizeThreshold = 1024 * 1024     // 1MB
)
public class ReviewCreateServlet extends HttpServlet {
    
    private ReviewDAO reviewDAO;
    private UploadDAO uploadDAO;
    
    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
        uploadDAO = new UploadDAO();
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
        String sakeIdParam = request.getParameter("sakeId");
        String ratingParam = request.getParameter("rating");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String sweetnessScoreParam = request.getParameter("sweetnessScore");
        String drynessScoreParam = request.getParameter("drynessScore");
        String acidityScoreParam = request.getParameter("acidityScore");
        String aromaScoreParam = request.getParameter("aromaScore");
        String bodyScoreParam = request.getParameter("bodyScore");
        
        // 유효성 검사
        if (sakeIdParam == null || ratingParam == null || content == null || 
            content.trim().isEmpty() || content.length() < 10) {
            request.setAttribute("error", "필수 항목을 입력해주세요. (별점, 리뷰 내용 최소 10자)");
            request.getRequestDispatcher("/review/form?sakeId=" + sakeIdParam).forward(request, response);
            return;
        }
        
        try {
            int sakeId = Integer.parseInt(sakeIdParam);
            int rating = Integer.parseInt(ratingParam);
            
            // 리뷰 객체 생성
            SakeReview review = new SakeReview();
            review.setSakeId(sakeId);
            review.setUserId(user.getUserId());
            review.setRating(rating);
            review.setTitle(title != null ? title.trim() : null);
            review.setContent(content.trim());
            
            // 맛 평가 점수 설정
            if (sweetnessScoreParam != null && !sweetnessScoreParam.isEmpty()) {
                review.setSweetnessScore(Integer.parseInt(sweetnessScoreParam));
            }
            if (drynessScoreParam != null && !drynessScoreParam.isEmpty()) {
                review.setDrynessScore(Integer.parseInt(drynessScoreParam));
            }
            if (acidityScoreParam != null && !acidityScoreParam.isEmpty()) {
                review.setAcidityScore(Integer.parseInt(acidityScoreParam));
            }
            if (aromaScoreParam != null && !aromaScoreParam.isEmpty()) {
                review.setAromaScore(Integer.parseInt(aromaScoreParam));
            }
            if (bodyScoreParam != null && !bodyScoreParam.isEmpty()) {
                review.setBodyScore(Integer.parseInt(bodyScoreParam));
            }
            
            // ML 서비스를 통한 맛 태그 분석 (비동기 처리 가능하지만 간단하게 동기 처리)
            try {
                String mlTags = MLClient.analyzeTaste(content);
                if (mlTags != null && !mlTags.isEmpty()) {
                    review.setMlTags(mlTags);
                }
            } catch (Exception e) {
                // ML 서비스 실패해도 리뷰는 저장
                System.err.println("ML 서비스 맛 분석 실패: " + e.getMessage());
            }
            
            // 리뷰 저장
            int reviewId = reviewDAO.insert(review);
            
            if (reviewId > 0) {
                // 파일 업로드 처리
                try {
                    List<Part> photoParts = new java.util.ArrayList<>();
                    for (Part part : request.getParts()) {
                        if ("photos".equals(part.getName()) && part.getSize() > 0) {
                            photoParts.add(part);
                        }
                    }
                    
                    if (!photoParts.isEmpty()) {
                        String realPath = getServletContext().getRealPath("/");
                        Part[] partsArray = photoParts.toArray(new Part[0]);
                        List<String> uploadedFiles = FileUploadUtil.uploadFiles(partsArray, realPath);
                        
                        // 업로드된 파일 정보를 DB에 저장
                        for (String filePath : uploadedFiles) {
                            uploadDAO.insert(user.getUserId(), reviewId, filePath);
                        }
                    }
                } catch (Exception e) {
                    // 파일 업로드 실패해도 리뷰는 저장됨
                    e.printStackTrace();
                }
                
                // 성공 시 사케 상세 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/sake/detail?sakeId=" + sakeId);
            } else {
                request.setAttribute("error", "리뷰 작성에 실패했습니다.");
                request.getRequestDispatcher("/review/form?sakeId=" + sakeId).forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "잘못된 입력값입니다.");
            request.getRequestDispatcher("/review/form?sakeId=" + sakeIdParam).forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
            request.getRequestDispatcher("/review/form?sakeId=" + sakeIdParam).forward(request, response);
        }
    }
}

