package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.Sake;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 사케 목록 서블릿
 */
@WebServlet("/sake/list")
public class SakeListServlet extends HttpServlet {
    
    private SakeDAO sakeDAO;
    
    @Override
    public void init() throws ServletException {
        sakeDAO = new SakeDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 필터 파라미터
        String region = request.getParameter("region");
        String style = request.getParameter("style");
        String keyword = request.getParameter("keyword");
        
        // 페이징 파라미터
        int page = 1;
        int size = 12;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            String sizeParam = request.getParameter("size");
            if (sizeParam != null && !sizeParam.isEmpty()) {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException e) {
            // 기본값 사용
        }
        
        try {
            List<Sake> sakeList;
            
            // 검색/필터 처리 (검색어가 있으면 검색, 없으면 필터 적용)
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 검색어가 있으면 검색 수행
                List<Sake> searchResults = sakeDAO.search(keyword.trim());
                sakeList = new ArrayList<>();
                
                // 추가 필터 적용 (지역, 스타일)
                for (Sake sake : searchResults) {
                    boolean matches = true;
                    
                    if (region != null && !region.isEmpty()) {
                        if (!region.equals(sake.getRegionPrefecture())) {
                            matches = false;
                        }
                    }
                    
                    if (style != null && !style.isEmpty()) {
                        if (!style.equals(sake.getStyle())) {
                            matches = false;
                        }
                    }
                    
                    if (matches) {
                        sakeList.add(sake);
                    }
                }
            } else if (region != null && !region.isEmpty()) {
                sakeList = sakeDAO.findByRegion(region);
                // 스타일 필터 추가 적용
                if (style != null && !style.isEmpty()) {
                    List<Sake> filtered = new ArrayList<>();
                    for (Sake sake : sakeList) {
                        if (style.equals(sake.getStyle())) {
                            filtered.add(sake);
                        }
                    }
                    sakeList = filtered;
                }
            } else if (style != null && !style.isEmpty()) {
                sakeList = sakeDAO.findByStyle(style);
            } else {
                sakeList = sakeDAO.findAll();
            }
            
            // 페이징 처리
            int totalCount = sakeList.size();
            int totalPages = (int) Math.ceil((double) totalCount / size);
            int start = (page - 1) * size;
            int end = Math.min(start + size, totalCount);
            
            List<Sake> pagedList = sakeList.subList(start, end);
            
            // 리뷰 통계를 위해 ReviewDAO도 필요하지만, 일단 기본 정보만 전달
            request.setAttribute("sakeList", pagedList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", size);
            request.setAttribute("region", region);
            request.setAttribute("style", style);
            request.setAttribute("keyword", keyword);
            
            request.getRequestDispatcher("/jsp/sake/list.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/sake/list.jsp").forward(request, response);
        }
    }
}

