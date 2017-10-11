package com.jiuchunjiaoyu.micro.wzb.task.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    private static final String dataFormatStr = "yyyy-MM-dd HH:mm:ss";

    public static String getDateBefor(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        SimpleDateFormat format = new SimpleDateFormat(dataFormatStr);
        return format.format(c.getTime());
    }

}
