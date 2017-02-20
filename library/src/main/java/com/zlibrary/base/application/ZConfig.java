package com.zlibrary.base.application;


public class ZConfig {

    /**
     * 默认的SessionKey值
     */
    public static final String SESSION_KEY = "SESSIONID";

    /**
     * 默认的等待数据超时时间
     */
    public static final int SO_TIMEOUT = 20 * 1000;

    /**
     * 默认的请求超时时间
     */
    public static final int REQUEST_TIMEOUT = 20 * 1000;

    /**
     * 日志文件存放目录
     */
    public static final String CRASH_CACHE = "CrashCache";
}
