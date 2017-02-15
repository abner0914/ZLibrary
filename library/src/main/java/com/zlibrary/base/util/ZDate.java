package com.zlibrary.base.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * 日期 时间 工具类
 */
public class ZDate {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 15:03:34
     */
    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    /**
     * 15:03
     */
    public static final String FORMAT_HH_MM = "HH:mm";
    /**
     * 12月12日
     */
    public static final String FORMAT_MM_DD = "MM月dd日";
    /**
     * 20120219
     */
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    /**
     * 2012-02-19
     */
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 2012-02-19
     */
    public static final String FORMAT_YYYY_MM_DD_ZH = "yyyy年MM月dd日";
    /**
     * 2012-02-19 05:11
     */
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 20120219150334
     */
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    /**
     * 20120219150334
     */
    public static final String FORMAT_DATABASE = "yyyyMMddHHmmss";
    /**
     * 20120219150334
     */
    public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    /**
     * 2012-02-19 15:03:34
     */
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 2012-02-19 15:03:34.876
     */
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 20120219 15:03:34.876
     */
    public static final String FORMAT_YYYYMMDD_HH_MM_SS_SSS = "yyyyMMdd HH:mm:ss.SSS";
    /**
     * 2012-02-19 12时19分
     */
    public static final String FORMAT_YYYY_MM_DD_HHMM_WORD = "yyyy-MM-dd HH时mm分";
    /**
     * 2002-19 12:19
     */
    public static final String FORMAT_MM_DD_HHMM_WORD = "MM-dd HH:mm";
    /**
     * 2012年02月19日12:19
     */
    public static final String FORMAT_YYYY_MM_DD_HHMM_WORD_ZH = "yyyy年MM月dd日HH:mm";

    public final static int TIME_DAY_MILLISECOND = 86400000;

    private ZDate() {
    }

    /**
     * 得到当前日期时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime() {
        return getCustomTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前日期
     *
     * @return yyyy-MM-dd
     */
    public static String getDate() {
        return getCustomTime("yyyy-MM-dd");
    }

    /**
     * 得到当前时间
     *
     * @return HH:mm:ss
     */
    public static String getTime() {
        return getCustomTime("HH:mm:ss");
    }

    /**
     * 获取自定义格式时间
     *
     * @param format 格式：yyyy年、MM月、dd日、HH小时、mm分钟、ss秒
     * @return
     */
    public static String getCustomTime(String format) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    /**
     * 传入String，获得一个Date对象
     *
     * @param strDate
     * @return yyyy-MM-dd HH:mm:ss 返回null为字符串错误
     */
    public static Date getDateTime(String strDate) {
        return getDateByFormat(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 传入String(例20130101210000)，获得一个自定义格式的Date(yyyy-MM-dd HH:mm:ss)对象
     *
     * @param strDate
     * @param format  格式
     * @return 返回null为字符串错误
     */
    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return (sdf.parse(strDate));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 传入一个时间，判断是星期几
     *
     * @param myDate
     * @return
     */
    public static String getWeekDay(Date myDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        int x = calendar.get(Calendar.DAY_OF_WEEK);
        String[] days = new String[]{"星期天", "星期一", "星期二", "星期三", "星期四",
                "星期五", "星期六"};
        if (x > 7) {
            return "error";
        }
        return days[x - 1];
    }

    /**
     * 传入如yyyyMMddHHmmss格式字符串，计算与当前时间距离多久
     *
     * @param timer yyyyMMddHHmmss 格式
     * @return 时间
     * @throws ParseException
     */
    public static String getComputeNowTimeStr(String timer, String format)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(timer);
        Date today = new Date();
        long interval = today.getTime() - date.getTime();
        today.setHours(23);
        today.setMinutes(59);
        today.setSeconds(59);
        boolean isSameDay = (interval / 86400000) == 0 ? true : false;
        if (isSameDay) {
            if (interval < 60 * 60 * 1000) {
                return (interval / (60 * 1000)) + "分钟前";
            } else {
                return (interval / (60 * 60 * 1000)) + "小时前";
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
            return dateFormat.format(date);
        }
    }

    /**
     * @param dateStr 日期字符串
     * @param format  日期字符串样式
     * @return Date对象
     * @Title: formatString2Date
     * @Description: 将日期字符串转换成Date对象
     * @date: 2013-5-22 下午2:07:29
     */
    public static Date formatString2Date(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param date   日期对象
     * @param format 日期字符串样式
     * @return 日期字符串
     * @Title: formatDate2String
     * @Description: 将日期对象转换成日期字符串
     * @date: 2013-5-22 下午2:12:17
     */
    public static String formatDate2String(Date date, String format) {
        if (date == null) {
            return "";
        }

        try {
            SimpleDateFormat formatPattern = new SimpleDateFormat(format);
            return formatPattern.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Description:转换数据库时间为UI显示样式
     *
     * @param dbTime     数据库查询出的时间<br>
     *                   数据库内的时间格式统一为yyyyMMddHHmmss
     * @param formatType 目标格式
     * @return 目标格式的字符串
     */
    public static String convertDBOperateTime2SpecifyFormat(String dbTime,
                                                            String formatType) {
        // 转换数据库时间为UI显示样式
        return getNewFormatDateString(dbTime, FORMAT_DATABASE, formatType);
    }

    /**
     * @param dateStr    日期字符串
     * @param fromFormat 原始样式
     * @return 数据库样式的日期字符串
     * @Title: formatDateString
     * @Description: 将日期字符串格式化成数据库样式
     * @date: 2013-5-22 上午10:15:35
     */
    public static String getDBOperateTime(String dateStr, String fromFormat) {
        // 1、将原始日期字符串转换成Date对象
        Date date = formatString2Date(dateStr, fromFormat);

        // 2、将Date对象转换成数据库样式样式字符串
        return formatDate2String(date, FORMAT_DATABASE);
    }

    /**
     * Description:所有数据库操作时间取值都采用本方法
     *
     * @return 当前时间yyyyMMddHHmmss格式，如：20120219111945
     */
    public static String getDBOperateTime() {
        return getCurrentTimeSpecifyFormat(FORMAT_DATABASE);
    }

    /**
     * Description:获取当前日期指定样式字符串
     *
     * @param formatType 指定的日期样式
     * @return 当前日期指定样式字符串
     */
    public static String getCurrentTimeSpecifyFormat(String formatType) {
        Date date = new Date();

        return formatDate2String(date, formatType);
    }

    /**
     * Description:获取昨天指定样式字符串
     *
     * @param formatType 指定的日期样式
     * @return 昨天日期指定样式字符串
     */
    public static String getYesterdaySpecifyFormat(String formatType) {
        Date date = new Date();
        date = new Date(date.getTime() - 1000 * 60 * 60 * 24);
        return formatDate2String(date, formatType);
    }

    /**
     * @param dateStr    日期字符串
     * @param fromFormat 原始样式
     * @param toFormat   目标样式
     * @return 目标样式的日期字符串
     * @Title: getNewFormatDateString
     * @Description: 格式化日期字符串
     * @date: 2013-5-22 上午10:15:35
     */
    public static String getNewFormatDateString(String dateStr,
                                                String fromFormat, String toFormat) {

        // 1、将原始日期字符串转换成Date对象
        Date date = formatString2Date(dateStr, fromFormat);

        // 2、将Date对象转换成目标样式字符串
        return formatDate2String(date, toFormat);
    }

    /**
     * @param dateFrom 起始日期
     * @param dateTo   结束日期
     * @return 日期之间的间隔天数
     * @Title: realDateIntervalDay
     * @Description: 计算日期之间的间隔天数
     * @date: 2013-5-22 下午2:25:28
     */
    public static int realDateIntervalDay(Date dateFrom, Date dateTo) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(dateFrom);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(dateTo);

        if (fromCal.after(toCal)) {
            // swap dates so that fromCal is start and toCal is end
            Calendar swap = fromCal;
            fromCal = toCal;
            toCal = swap;
        }
        int days = toCal.get(Calendar.DAY_OF_YEAR)
                - fromCal.get(Calendar.DAY_OF_YEAR);
        int y2 = toCal.get(Calendar.YEAR);
        if (fromCal.get(Calendar.YEAR) != y2) {
            fromCal = (Calendar) fromCal.clone();
            do {
                // 得到当年的实际天数
                days += fromCal.getActualMaximum(Calendar.DAY_OF_YEAR);
                fromCal.add(Calendar.YEAR, 1);
            } while (fromCal.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获得两个Date型日期之间相差的天数（第2个减第1个）
     *
     * @param first, second
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(Date first, Date second) {
        Date d1 = formatString2Date(formatDate2String(first, FORMAT_YYYY_MM_DD), FORMAT_YYYY_MM_DD);
        Date d2 = formatString2Date(formatDate2String(second, FORMAT_YYYY_MM_DD), FORMAT_YYYY_MM_DD);

        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);

        return mils.intValue();
    }

    /**
     * 获得两个String型日期之间相差的天数（第2个减第1个）
     *
     * @param first, second
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(String first, String second) {
        Date d1 = formatString2Date(first, FORMAT_YYYY_MM_DD);
        Date d2 = formatString2Date(second, FORMAT_YYYY_MM_DD);

        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);

        return mils.intValue();
    }

    /**
     * @param longTime
     * @param format
     * @return
     * @Title: getDateTimeByFormatAndMs
     * @Description: 将毫秒型日期转换成指定格式日期字符串
     * @date: 2013-2-25 下午12:00:51
     */
    public static String getDateTimeByFormatAndMs(long longTime, String format) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(longTime);

        return formatDate2String(c.getTime(), format);
    }

    /**
     * 时间转毫秒数
     *
     * @return
     */
    public static long getTimes(String date) {
        long lTime = 0;
        try {
            Date dt = DEFAULT_DATE_FORMAT.parse(date);
            lTime = dt.getTime();// 继续转换得到秒数的long型
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lTime;
    }
}
