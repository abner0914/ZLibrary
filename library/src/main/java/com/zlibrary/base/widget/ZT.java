package com.zlibrary.base.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ZLibrary.R;
import com.ZLibrary.base.application.ZApplication;
import com.ZLibrary.base.util.ZFormat;

/**
 * 自定义 Toast
 */
public class ZT {

    /**
     * Toast View 的 LayoutParams
     */
    private static final ViewGroup.LayoutParams M_LAYOUT_PARAMS = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    /**
     * Toast View 的 TextColor
     */
    private static final int TEXT_COLOR = Color.rgb(250, 250, 250);
    /**
     * Toast View 的 TextSize;
     */
    private static final int TEXT_SIZE = 16;
    private static Context mContext;
    private static Toast t;
    private static GradientDrawable mGradientDrawable;

    /**
     * 禁止实例化
     */
    private ZT() {
    }

    /**
     * 显示全屏，位置自定义Toast
     *
     * @param text 内容
     * @param y    向下偏移高度
     */
    public static void st(String text, int y) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);
        //设置透明度
        layout.getBackground().setAlpha(100);
        TextView tv = (TextView) layout.findViewById(R.id.toast_tv);
        if (t != null) {
            t.cancel();
        }
        t = new Toast(getContext());
        tv.setText(text);
        t.setView(layout);
        t.setDuration(Toast.LENGTH_SHORT);
        //显示位置：横向全屏、顶部
        t.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, y);
        t.show();
    }

    /**
     * 显示短Toast
     *
     * @param text
     */
    public static void ss(int text) {
        s(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param text
     */
    public static void ss(CharSequence text) {
        s(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param context
     * @param text
     */
    public static void ss(Context context, int text) {
        s(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param context
     * @param text
     */
    public static void ss(Context context, CharSequence text) {
        s(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长Toast
     *
     * @param text
     */
    public static void sl(int text) {
        s(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param text
     */
    public static void sl(CharSequence text) {
        s(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param context
     * @param text
     */
    public static void sl(Context context, int text) {
        s(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param context
     * @param text
     */
    public static void sl(Context context, CharSequence text) {
        s(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param text
     * @param duration
     */
    public static void s(int text, int duration) {
        s(getContext(), text, duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param text
     * @param duration
     */
    public static void s(CharSequence text, int duration) {
        s(getContext(), text, duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void s(Context context, int text, int duration) {
        s(context, getText(text), duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void s(Context context, CharSequence text, int duration) {
        mContext = context;
        makeToast(context, text, duration).show();
    }

    private static Context getContext() {
        if (mContext == null)
            mContext = ZApplication.getInstance().getApplicationContext();
        return mContext;
    }

    private static String getText(int text) {
        return getContext().getResources().getString(text);
    }

    public static Toast makeToast(Context context, CharSequence text,
                                  int duration) {
        View mToastView = getToastView(context, text);
        Toast result = new Toast(context);
        result.setView(mToastView);
        result.setDuration(duration);
        return result;
    }

    private static View getToastView(Context context, CharSequence text) {
        int padding = ZFormat.dip2px(context, 10);
        TextView mTextView = new TextView(context);
        mTextView.setLayoutParams(M_LAYOUT_PARAMS);
        mTextView.setPadding(padding, padding, padding, padding);
        mTextView.setBackgroundDrawable(getViewGradientDrawable(padding / 2));
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
        mTextView.setTextColor(TEXT_COLOR);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setText(text);
        return mTextView;
    }

    private static GradientDrawable getViewGradientDrawable(int radius) {
        if (mGradientDrawable == null) {
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setCornerRadius(radius);
            mGradientDrawable.setColor(Color.argb(160, 0, 0, 0));
        }
        return mGradientDrawable;
    }

}
