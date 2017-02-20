package com.zlibrary.base.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;

/**
 * 对话框工具类
 */
public class ZDialog {

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (msg != null) builder.setMessage(msg);
        if (title != null) builder.setTitle(title);
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, String title, String msg, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (msg != null) builder.setMessage(Html.fromHtml(msg));
        if (title != null) builder.setTitle(title);
        return builder;
    }


    public static AlertDialog.Builder dialogBuilder(Context context, int title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (view != null) builder.setView(view);
        if (title > 0) builder.setTitle(title);
        return builder;
    }

    public static AlertDialog.Builder dialogBuilder(Context context, int titleResId, int msgResId) {
        String title = titleResId > 0 ? context.getResources().getString(titleResId) : null;
        String msg = msgResId > 0 ? context.getResources().getString(msgResId) : null;
        return dialogBuilder(context, title, msg);
    }

    public static Dialog showTips(Context context, String title, String des) {
        AlertDialog.Builder builder = ZDialog.dialogBuilder(context, title, des);
        builder.setCancelable(true);
        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showTips(Context context, int title, int des) {
        return showTips(context, context.getString(title), context.getString(des));
    }

    public static Dialog showTipsWithBtn(Context context, String title, String des) {
        AlertDialog.Builder builder = ZDialog.dialogBuilder(context, title, des);
        builder.setPositiveButton("确定", null);
        builder.setCancelable(true);
        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
