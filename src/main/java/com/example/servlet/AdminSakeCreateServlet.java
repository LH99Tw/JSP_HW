package com.example.servlet;

import com.example.dao.SakeDAO;
import com.example.model.Sake;
import com.example.model.User;
import com.example.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * 관리자 사케 등록 서블릿
 */
@WebServlet("/admin/sake/create")
public class AdminSakeCreateServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 관리자 권한 체크
        User user = SessionUtil.getUser(request.getSession(false));
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // 파라미터 받기
        String nameJa = request.getParameter("nameJa");
        String nameEn = request.getParameter("nameEn");
        String nameKo = request.getParameter("nameKo");
        String brand = request.getParameter("brand");
        String brewery = request.getParameter("brewery");
        String region = request.getParameter("region");
        String style = request.getParameter("style");
        String alcoholPercentStr = request.getParameter("alcoholPercent");
        String volumeMlStr = request.getParameter("volumeMl");
        String polishingRatioStr = request.getParameter("polishingRatio");
        String nihonshuDoStr = request.getParameter("nihonshuDo");
        String acidityStr = request.getParameter("acidity");
        String sweetnessLevelStr = request.getParameter("sweetnessLevel");
        String aromaType = request.getParameter("aromaType");
        String bodyLevelStr = request.getParameter("bodyLevel");
        String thumbnailPath = request.getParameter("thumbnailPath");
        
        // 서버 측 유효성 검사
        if (nameJa == null || nameJa.trim().isEmpty() || 
            nameKo == null || nameKo.trim().isEmpty()) {
            request.setAttribute("error", "사케명(일본어, 한국어)은 필수입니다.");
            request.getRequestDispatcher("/jsp/admin/sake/form.jsp").forward(request, response);
            return;
        }
        
        try {
            Sake sake = new Sake();
            sake.setNameJa(nameJa.trim());
            sake.setNameEn(nameEn != null ? nameEn.trim() : null);
            sake.setNameKo(nameKo.trim());
            sake.setBrand(brand != null ? brand.trim() : null);
            sake.setBrewery(brewery != null ? brewery.trim() : null);
            sake.setRegionPrefecture(region != null ? region.trim() : null);
            sake.setStyle(style != null ? style.trim() : null);
            sake.setAlcoholPercent(alcoholPercentStr != null && !alcoholPercentStr.isEmpty() ? 
                                   new BigDecimal(alcoholPercentStr) : null);
            sake.setVolumeMl(volumeMlStr != null && !volumeMlStr.isEmpty() ? 
                            Integer.parseInt(volumeMlStr) : null);
            sake.setPolishingRatio(polishingRatioStr != null && !polishingRatioStr.isEmpty() ? 
                                  Integer.parseInt(polishingRatioStr) : null);
            sake.setNihonshuDo(nihonshuDoStr != null && !nihonshuDoStr.isEmpty() ? 
                              new BigDecimal(nihonshuDoStr) : null);
            sake.setAcidity(acidityStr != null && !acidityStr.isEmpty() ? 
                           new BigDecimal(acidityStr) : null);
            sake.setSweetnessLevel(sweetnessLevelStr != null && !sweetnessLevelStr.isEmpty() ? 
                                  Integer.parseInt(sweetnessLevelStr) : null);
            sake.setAromaType(aromaType != null ? aromaType.trim() : null);
            sake.setBodyLevel(bodyLevelStr != null && !bodyLevelStr.isEmpty() ? 
                            Integer.parseInt(bodyLevelStr) : null);
            sake.setThumbnailPath(thumbnailPath != null ? thumbnailPath.trim() : null);
            
            SakeDAO sakeDAO = new SakeDAO();
            int sakeId = sakeDAO.insert(sake);
            
            if (sakeId > 0) {
                response.sendRedirect(request.getContextPath() + "/admin/sake/list");
            } else {
                request.setAttribute("error", "사케 등록에 실패했습니다.");
                request.getRequestDispatcher("/jsp/admin/sake/form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "사케 등록 중 오류가 발생했습니다: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/sake/form.jsp").forward(request, response);
        }
    }
}

