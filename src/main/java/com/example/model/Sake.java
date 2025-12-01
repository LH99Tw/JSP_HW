package com.example.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 사케 모델 클래스
 */
public class Sake {
    private int sakeId;
    private String nameJa;
    private String nameEn;
    private String nameKo;
    private String brand;
    private String brewery;
    private String regionPrefecture;
    private String style;
    private BigDecimal alcoholPercent;
    private Integer volumeMl;
    private Integer polishingRatio;
    private BigDecimal nihonshuDo;
    private BigDecimal acidity;
    private Integer sweetnessLevel; // 1=카라구치, 5=아마구치
    private String aromaType;
    private Integer bodyLevel; // 1=라이트, 5=풀바디
    private String thumbnailPath;
    private Timestamp createdAt;
    
    // 기본 생성자
    public Sake() {
    }
    
    // Getters and Setters
    public int getSakeId() {
        return sakeId;
    }
    
    public void setSakeId(int sakeId) {
        this.sakeId = sakeId;
    }
    
    public String getNameJa() {
        return nameJa;
    }
    
    public void setNameJa(String nameJa) {
        this.nameJa = nameJa;
    }
    
    public String getNameEn() {
        return nameEn;
    }
    
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public String getNameKo() {
        return nameKo;
    }
    
    public void setNameKo(String nameKo) {
        this.nameKo = nameKo;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getBrewery() {
        return brewery;
    }
    
    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }
    
    public String getRegionPrefecture() {
        return regionPrefecture;
    }
    
    public void setRegionPrefecture(String regionPrefecture) {
        this.regionPrefecture = regionPrefecture;
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public BigDecimal getAlcoholPercent() {
        return alcoholPercent;
    }
    
    public void setAlcoholPercent(BigDecimal alcoholPercent) {
        this.alcoholPercent = alcoholPercent;
    }
    
    public Integer getVolumeMl() {
        return volumeMl;
    }
    
    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }
    
    public Integer getPolishingRatio() {
        return polishingRatio;
    }
    
    public void setPolishingRatio(Integer polishingRatio) {
        this.polishingRatio = polishingRatio;
    }
    
    public BigDecimal getNihonshuDo() {
        return nihonshuDo;
    }
    
    public void setNihonshuDo(BigDecimal nihonshuDo) {
        this.nihonshuDo = nihonshuDo;
    }
    
    public BigDecimal getAcidity() {
        return acidity;
    }
    
    public void setAcidity(BigDecimal acidity) {
        this.acidity = acidity;
    }
    
    public Integer getSweetnessLevel() {
        return sweetnessLevel;
    }
    
    public void setSweetnessLevel(Integer sweetnessLevel) {
        this.sweetnessLevel = sweetnessLevel;
    }
    
    public String getAromaType() {
        return aromaType;
    }
    
    public void setAromaType(String aromaType) {
        this.aromaType = aromaType;
    }
    
    public Integer getBodyLevel() {
        return bodyLevel;
    }
    
    public void setBodyLevel(Integer bodyLevel) {
        this.bodyLevel = bodyLevel;
    }
    
    public String getThumbnailPath() {
        return thumbnailPath;
    }
    
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // 편의 메서드
    public String getDisplayName() {
        if (nameKo != null && !nameKo.isEmpty()) {
            return nameKo;
        } else if (nameEn != null && !nameEn.isEmpty()) {
            return nameEn;
        } else {
            return nameJa;
        }
    }
}

