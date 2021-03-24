package com.boaz.adv_Backend.util;

import com.boaz.adv_Backend.vo.NewsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateFormatConversion {
    public static void conversion(List<NewsList> news) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        for (NewsList n : news) {
            if (n.getDate().substring(0, 10).equals(today)) {
                n.setDate(n.getDate().substring(11, 16));
            } else {
                n.setDate(n.getDate().substring(0, 10));
            }
        }
    }
}
