package com.eastx.jt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
    public final static SimpleDateFormat sdfPrint = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final static SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * since 1970-01-01 00:00:00.000
     */
    private final static long sysBase = 0;
    private final static long locBase = str2num("1990-01-01");

    public static long str2num(String dateStr) {
        try {
            return sdfParse.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static long str2num(String dateStr, DateFormat format) throws ParseException {
        return format.parse(dateStr).getTime();
    }

    public static String num2Str(long dateNum) {
        return sdfPrint.format(new Date(dateNum));
    }

    public static Date num2date(long dateNum) {
        return new Date(dateNum);
    }

    public static long date2num(Date date) {
        return date.getTime();
    }


    public static void main(String[] argv) throws ParseException {
        String fa = "yyyy-MM-dd HH:mm:ss";
        String sa = "1970-01-01 12:00:00";
        String za = "GMT+8";

        SimpleDateFormat sdf1 = new SimpleDateFormat(fa);
        sdf1.setTimeZone(TimeZone.getTimeZone(za));

        long la = sdf1.parse(sa).getTime();

        System.out.println(String.format("format:%s // str:%s // long:%d // zone:%s", fa, sa, la, za));

        za = "GMT+8";
        sdf1.setTimeZone(TimeZone.getTimeZone(za));
        sa = sdf1.format(new Date(la));

        System.out.println(String.format("format:%s // str:%s // long:%d // zone:%s", fa, sa, la, za));

        za = "GMT+0";
        sdf1.setTimeZone(TimeZone.getTimeZone(za));
        sa = sdf1.format(new Date(la));

        System.out.println(String.format("format:%s // str:%s // long:%d // zone:%s", fa, sa, la, za));

        za = "GMT+9";
        sdf1.setTimeZone(TimeZone.getTimeZone(za));
        sa = sdf1.format(new Date(la));

        System.out.println(String.format("format:%s // str:%s // long:%d // zone:%s", fa, sa, la, za));

//        long loc_a = std_a - base;
//
//        Date a = num2date(loc_a);
//
//        System.out.println(sdfPrint.format(a));
//
        TimeZone timeZone = TimeZone.getTimeZone("GTM+8");
        TimeZone.setDefault(timeZone);
//
//        SimpleDateFormat tokyouSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        tokyouSdf.setTimeZone(TimeZone.getTimeZone("GTM+0"));
//        TimeZone timeZone = TimeZone.getTimeZone("GTM+9");
        SimpleDateFormat tokyouSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        tokyouSdf.setTimeZone(TimeZone.getDefault());
        System.out.println(tokyouSdf.format(new Date(0)));
    }
}
