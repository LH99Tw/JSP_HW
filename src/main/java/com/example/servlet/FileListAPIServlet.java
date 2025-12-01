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
import com.example.util.JSONUtil;

/**
 * 파일 목록 API 서블릿 (JSON)
 */
@WebServlet("/api/files")
public class FileListAPIServlet extends HttpServlet {
    
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
        
        try {
            // 사용자의 모든 파일 가져오기
            List<FileInfo> files = fileDAO.findByUserId(user.getUserId());
            
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            String filesJson = JSONUtil.filesToJson(files);
            String result = "{\"success\":true,\"files\":" + filesJson + ",\"count\":" + files.size() + "}";
            out.print(result);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(JSONUtil.errorJson("파일 목록을 불러오는 중 오류가 발생했습니다."));
        }
    }
}

