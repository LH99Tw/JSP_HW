package com.example.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 날짜 변환 유틸리티
 */
public class DateUtil {
    
    /**
     * LocalDateTime을 Date로 변환
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

