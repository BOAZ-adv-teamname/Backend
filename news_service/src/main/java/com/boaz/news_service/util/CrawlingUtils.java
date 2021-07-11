package com.boaz.news_service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CrawlingUtils {
    public static String getDateStr(int d) {
        return getDateStr(Calendar.getInstance(),d);
    }

    private static String getDateStr(Calendar calendar, int d) {
        calendar.add(Calendar.DATE, d);
        return new SimpleDateFormat("YYYYMMdd").format(calendar.getTime());
    }
}