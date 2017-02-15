package com.zlibrary.base.util;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.zlibrary.base.application.ZApplication;


/**
 * 日志输出
 */
public class ZL {

    private static final String TAG = ZApplication.getInstance().getPackageName();
    private static Context mContext;

    /**
     * 禁止实例化
     */
    private ZL() {
    }

    /**
     * 打印消息类传入的字符串
     * @param msg
     */
    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * 打印DEBUG类传入的字符串
     * @param msg
     */
    public static void d(String msg) {
        d(TAG, msg);
    }

    /**
     * 打印ERROR类传入的字符串
     * @param msg
     */
    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * 打印详细类传入的字符串
     * @param msg
     */
    public static void v(String msg) {
        v(TAG, msg);
    }

    /**
     * 打印消息类传入的字符串
     * @param msg
     */
    public static void i(int msg) {
        i(TAG, msg);
    }

    /**
     * 打印DEBUG类传入的字符串
     * @param msg
     */
    public static void d(int msg) {
        d(TAG, msg);
    }

    /**
     * 打印ERROR类传入的字符串
     * @param msg
     */
    public static void e(int msg) {
        e(TAG, msg);
    }

    /**
     * 打印详细类传入的字符串
     * @param msg
     */
    public static void v(int msg) {
        v(TAG, msg);
    }

    /**
     * 打印消息类传入的字符串
     * @param tag
     * @param message
     */
    public static void i(String tag, int message) {
        String str = getContext().getResources().getString(message);
        i(tag, str);
    }

    /**
     * 打印DEBUG类传入的字符串
     * @param tag
     * @param message
     */
    public static void d(String tag, int message) {
        String str = getContext().getResources().getString(message);
        d(tag, str);
    }

    /**
     * 打印ERROR类传入的字符串
     * @param tag
     * @param message
     */
    public static void e(String tag, int message) {
        String str = getContext().getResources().getString(message);
        e(tag, str);
    }

    /**
     * 打印详细类传入的字符串
     * @param tag
     * @param message
     */
    public static void v(String tag, int message) {
        String str = getContext().getResources().getString(message);
        v(tag, str);
    }

    /**
     * 打印消息类传入的字符串
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.t(tag).i(msg);
    }

    /**
     * 打印DEBUG类传入的字符串
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.t(tag).d(msg);
    }

    /**
     * 打印ERROR类传入的字符串
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.t(tag).e(msg);
    }

    /**
     * 打印详细类传入的字符串
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.t(tag).v(msg);
    }

    /**
     * 打印json格式字符串
     *
     * @param jsonStr
     */
    public static void json(String jsonStr) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.json(jsonStr);
    }

    /**
     * 打印xml格式字符串
     *
     * @param xmlStr
     */
    public static void xml(String xmlStr) {
        if (ZApplication.getInstance().getIsOpenDebugMode())
            Logger.xml(xmlStr);
    }

    private static Context getContext() {
        if (mContext == null)
            mContext = ZApplication.getInstance().getContext();
        return mContext;
    }

}
