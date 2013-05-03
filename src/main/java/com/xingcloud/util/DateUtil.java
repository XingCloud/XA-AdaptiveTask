package com.xingcloud.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    public static int getDateDistance(String startDate, String endDate) {
        int distance = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long begin = sdf.parse(startDate).getTime();
            long end = sdf.parse(endDate).getTime();
            distance = (int) (end - begin) / 86400000;
        } catch (Exception e) {
            // Do nothing
        }
        return distance;
    }
    /*
    public static String getTodayDate(){
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setTimeZone(tz);
        return sdf.format(new Date()) ;

    } */
    public static String getDateByDistance(int distance){
        TimeZone tz = TimeZone.getTimeZone("GMT+8") ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
        sdf.setTimeZone(tz);
        Calendar cal = Calendar.getInstance(tz) ;
        cal.add(Calendar.DAY_OF_MONTH,distance);
        return  sdf.format(cal.getTime()) ;
    }
}
