package com.laughter.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * created by JH at 2019/4/15
 * des： com.laughter.framework.util
 */
public class DateUtil {

    public static String date2String(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return mSimpleDateFormat.format(date);
    }
}
