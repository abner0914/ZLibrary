package com.zlibrary.base.listener;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

/**
 * 短信监听类
 */
public class SmsListener extends ContentObserver {
    private static SmsListener listener;

    private Context ctx;
    private String spNum;
    private int start;
    private int len;
    private SmsCallback smsCallback;

    private SmsListener(Context ctx, String spNum, int start, int len, SmsCallback smsCallback) {
        super(new Handler());
        this.ctx = ctx;
        this.spNum = spNum;
        this.start = start;
        this.len = len;
        this.smsCallback = smsCallback;
    }

    //注册短信监听
    public static void register(Context ctx, String spNum, int start, int len, SmsCallback smsCallback) {
        if (listener == null) {
            listener = new SmsListener(ctx, spNum, start, len, smsCallback);
        }
        ctx.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, listener);
    }

    //注销短信监听
    public static void unregister(Context ctx) {
        if (listener != null) {
            ctx.getContentResolver().unregisterContentObserver(listener);
            listener = null;
        }
    }

    /**
     * 从短信内容中截取验证码
     *
     * @param body
     * @param start
     * @param len
     * @return
     */
    public static String parse(String body, int start, int len) {
        if (!TextUtils.isEmpty(body) && body.length() >= len) {
            if (len > 0) {
                body = body.substring(start, start + len);
            }
            return body;
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (!TextUtils.isEmpty(spNum)) {
            // 读取收件箱中指定号码的短信
            Cursor cursor = ((Activity) ctx).managedQuery(Uri.parse("content://sms/inbox"),
                    new String[]{"address", "body"}, "read = ?",
                    new String[]{"0"}, "date desc");

            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    String addr = cursor.getString(cursor.getColumnIndex("address"));
                    if (addr.startsWith("+86")) {
                        addr = addr.replace("+86", "");
                    }
                    String[] smsArr = spNum.split(",|;|\\|");
                    for (String no : smsArr) {
                        if (addr.startsWith(no)) {
                            String body = cursor.getString(cursor.getColumnIndex("body"));
                            body = parse(body, start, len);
                            if (smsCallback != null)
                                smsCallback.onReceive(addr, body);
                            break;
                        }
                    }
                }
            }
        }
    }

    public interface SmsCallback {
        void onReceive(String spNum, String body);
    }
}
