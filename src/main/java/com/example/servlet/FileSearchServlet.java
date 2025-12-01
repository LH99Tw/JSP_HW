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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import com.example.util.JSONUtil;

/**
 * 파일 검색 API 서블릿 (JSON)
 */
@WebServlet("/api/search")
public class FileSearchServlet extends HttpServlet {
    
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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(JSONUtil.errorJson("로그인이 필요합니다."));
            return;
        }
        
        String query = request.getParameter("q");
        
        try {
            // 사용자의 모든 파일 가져오기
            List<FileInfo> allFiles = fileDAO.findByUserId(user.getUserId());
            
            List<FileInfo> results;
            if (query == null || query.trim().isEmpty()) {
                // 검색어가 없으면 모든 파일 반환
                results = allFiles;
            } else {
                // 파일명으로 필터링 (대소문자 구분 없음)
                String searchQuery = query.toLowerCase().trim();
                results = allFiles.stream()
                    .filter(file -> file.getOriginalFilename().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
            }
            
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            String filesJson = JSONUtil.filesToJson(results);
            String queryEscaped = (query != null ? query : "").replace("\"", "\\\"");
            String result = "{\"success\":true,\"files\":" + filesJson + ",\"count\":" + results.size() + ",\"query\":\"" + queryEscaped + "\"}";
            out.print(result);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(JSONUtil.errorJson("검색 중 오류가 발생했습니다."));
        }
    }
}

