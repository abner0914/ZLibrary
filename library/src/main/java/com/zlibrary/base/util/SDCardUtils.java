package com.zlibrary.base.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 手机sd卡相关工具类
 */
public class SDCardUtils {

    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取手机的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(Context ctx) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        String filePath = getSdPath(ctx);
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取手机存储路径
     *
     * @param ctx
     * @return
     */
    public static String getSdPath(Context ctx) {
        if (isSDCardEnable()) {
            // 获取外部存储目录SDCard 根目
            return getSDCardPath();
        } else {
            // 应用系统存储目录
            return ctx.getCacheDir() + File.separator;
        }
    }

    /**
     * 判断是否有剩余空间
     *
     * @param ctx
     * @param size
     * @return
     */
    public static boolean checkSdSize(Context ctx, long size) {
        return getFreeBytes(ctx) > size;
    }
}
