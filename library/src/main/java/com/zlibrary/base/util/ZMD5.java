package com.zlibrary.base.util;

import com.zlibrary.base.exception.LException;

import java.security.MessageDigest;


/**
 * MD5加密工具类
 */
public final class ZMD5 {

    private ZMD5() {
    }

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHasnString(digest);
        } catch (Exception e) {
            ZL.e(LException.getStackMsg(e));
        }
        return null;
    }

    private static String getHasnString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}
