package com.zlibrary.base.util;

import android.content.Context;
import android.os.Build;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZFormat {

    /**
     * 禁止实例化
     */
    private ZFormat() {
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    /**
     * 判断字符串是否为空(包含null、""、NULL、"     "等内容)
     *
     * @param str
     * @return true为空 false不为空
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "null".equalsIgnoreCase(str)
                || "".equals(str.trim());
    }

    /**
     * 比较两个字符串是否相同
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return true为相同，false为不同
     */
    public static boolean isEqual(String str1, String str2) {
        if (isEmpty(str1))
            return false;
        if (isEmpty(str2))
            return false;
        return str1.equals(str2);
    }

    /**
     * 清理JSON无用字符
     *
     * @param json json
     * @return
     */
    public static String JSONTokener(String json) {
        if (isEmpty(json))
            return null;
        String res = json.replaceAll("\n", "");
        if (res.startsWith("\ufeff")) {
            res = res.substring(1);
        }
        return res;
    }

    /**
     * 传入两个字符串比较，相同为true
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isSame(String str1, String str2) {
        if (isEmpty(str1))
            return false;
        if (isEmpty(str2))
            return false;
        return str1.equals(str2);
    }

    /**
     * 判断是否为正确的邮件格式
     *
     * @param str
     * @return boolean
     */
    public static boolean isEmail(String str) {
        if (isEmpty(str))
            return false;
        return str.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    }

    /**
     * 判断字符串长度是否在此范围
     *
     * @param str ：需要比较的字符串
     * @param min ：最小值
     * @param max ：最大值
     * @return
     */
    public static boolean isLength(String str, int min, int max) {
        if (isEmpty(str))
            return false;
        return str.length() >= min && str.length() <= max;
    }

    /**
     * 判断字符串是否为合法手机号 11位 13 14 15 18开头
     *
     * @param str
     * @return boolean
     */
    public static boolean isMobile(String str) {
        if (isEmpty(str))
            return false;
        return str.matches("^(13|14|15|18)\\d{9}$");
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str))
            return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为浮点数或者整数
     *
     * @param str
     * @return true Or false
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str))
            return false;
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 将url转成MD5
     *
     * @param url
     * @return
     */
    public static String getMD5Url(String url) {
        return ZMD5.getMD5(url) + url.substring(url.lastIndexOf('.'));
    }

    /**
     * 传入两个数字，计算比例
     *
     * @param arg1
     * @param arg2
     * @return
     */
    public static double getScale(double arg1, double arg2) {
        if (arg2 == 0) {
            throw new ArithmeticException("by arg2 is zero");
        }
        return arg1 / arg2;
    }

    /**
     * 将数字转化为简写
     *
     * @param number
     * @return
     */
    public static String formatNumber(int number) {
        String numStr = String.valueOf(number);
        String result = numStr;
        if (numStr.toString().length() >= 5) {
            result = numStr.substring(0, numStr.length() - 4);
            // 控制最大只显示999W+
            if (result.length() > 3) {
                result = "999";
            }
            result = String.format("%sW+", result);
        }
        return result;
    }

}
