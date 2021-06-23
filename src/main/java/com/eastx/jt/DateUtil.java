package com.eastx.jt;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @ClassName dateUtil
 * @Description: TODO
 * @Author Tender
 * @Time 2021/6/14 17:04
 * @Version 1.0
 * @Since 1.8
 * @Copyright ©2021-2021 Tender Xie, All Rights Reserved.
 **/
public class DateUtil {
    /**
     * a global timezone for strategies
     */
    private static volatile TimeZone globalTimeZone = TimeZone.getDefault();

    /**
     *
     */
    private static SimpleDateFormat timeFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     *
     */
    private static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    /**
     *
     */
    private final static String TIME_BASE_STRING = "1990-01-01";

    /**
     *
     */
    private final static String TIME_BASE_ZONE = "GMT+0";

    /**
     *
     */
    private final static long TIME_BASE_LONG = getSafeNum(TIME_BASE_STRING, TimeZone.getTimeZone(TIME_BASE_ZONE));

    /**
     *
     */
    public static void setTimeZone(TimeZone zone) {
        globalTimeZone = checkNotNull(zone, "Input parameter %s should not be null.", "zone");
        dateFormater.setTimeZone(zone);
        timeFormater.setTimeZone(zone);
    }

    /**
     *
     * @return
     */
    public static TimeZone getTimeZone() {
        return globalTimeZone;
    }

    /**
     *
     * @param dateStr
     * @return
     */
    public static long parse(String dateStr) throws ParseException {
        return relative(dateFormater.parse(dateStr).getTime());
    }

    /**
     *
     */
    private static long relative(long absDateNum) {
        return absDateNum - TIME_BASE_LONG;
    }

    /**
     *
     */
    private static long absolute(long relaDateNum) {
        return relaDateNum + TIME_BASE_LONG;
    }

    /**
     *
     * @param dateStr
     * @return
     */
    public static long parse(String dateStr, SimpleDateFormat formatter) throws ParseException {
        formatter.setTimeZone(globalTimeZone);
        return relative(formatter.parse(dateStr).getTime());
    }

    /**
     *
     * @param dateStr
     * @return
     */
    private static long getSafeNum(String dateStr, TimeZone zone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(zone);
        long dateNum = 0;
        try {
            dateNum = formatter.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            dateNum = 0;
        } finally {
            return dateNum;
        }
    }

    /**
     *
     * @param dateNum
     * @return
     */
    public static String num2Str(long dateNum) {
        return dateFormater.format(num2date(dateNum));
    }

    /**
     *
     * @param dateNum
     * @return
     */
    public static Date num2date(long dateNum) {
        return new Date(absolute(dateNum));
    }

    /**
     *
     * @param date
     * @return
     */
    public static long date2num(Date date) {
        return relative(date.getTime());
    }

    public static void main(String[] argv) throws ParseException {
        String fa = "yyyy-MM-dd HH:mm:ss";
        String sa = "1970-01-01 00:00:00.000";
        String sb = "1998-03-02 12:20:00.000";
        String za = "GMT+0";
        String zb = "GMT+8";

        DateUtil.setTimeZone(TimeZone.getTimeZone(za));
        long lb0 = DateUtil.parse(sb, DateUtil.timeFormater);

        System.out.println(DateUtil.num2Str(lb0));
        System.out.println(DateUtil.TIME_BASE_LONG);

        DateUtil.setTimeZone(TimeZone.getTimeZone(zb));
        long lb8 = DateUtil.parse(sb, DateUtil.timeFormater);

        System.out.println(String.format("date=%s lb1(%s)=%d lb2(%s)=%d", sa, za, lb0, zb, lb8));

    }
}
