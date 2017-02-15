package com.zlibrary.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zlibrary.base.application.ZApplication;
import com.zlibrary.base.util.ZFormat;

/**
 * 等待框
 */
public class ZProgress extends Dialog implements DialogInterface.OnKeyListener {

    private static final int BACKGROUND_ALPHA = 150;
    private static final int BACKGROUND_COLOR = Color.rgb(255, 255, 255);
    private static final int TEXT_COLOR = Color.rgb(128, 128, 128);
    private static int width, height, padding10;
    private static GradientDrawable mGradientDrawable;
    private Context mContext;
    private DialogView mDialogView;
    private boolean mFlag = false;
    private OnKeyBackListener mOnKeyBackListener;

    public ZProgress(Context context) {
        super(context);
        mContext = context;
        width = ZApplication.getInstance().getDiaplayWidth() / 12 * 2;
        height = width;
        padding10 = ZFormat.dip2px(mContext, 10);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogView = new DialogView(mContext);
        getWindow().addContentView(mDialogView,
                new ViewGroup.LayoutParams(width, height));
        getWindow().getDecorView().setBackgroundDrawable(
                getViewGradientDrawable(padding10));
        getWindow().getDecorView().getBackground().setAlpha(BACKGROUND_ALPHA);
        setCancelable(mFlag);
        setOnKeyListener(this);
    }

    private static GradientDrawable getViewGradientDrawable(int radius) {
        if (mGradientDrawable == null) {
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setCornerRadius(radius);
            mGradientDrawable.setColor(BACKGROUND_COLOR);
        }
        return mGradientDrawable;
    }

    public void show(String text) {
        if (!isShowing()) {
            this.show();
        }
        mDialogView.setText(text);
    }

    public void setOnKeyBackListener(OnKeyBackListener keyBackListener) {
        this.mOnKeyBackListener = keyBackListener;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            if (mOnKeyBackListener != null) {
                mOnKeyBackListener.onKeyBackListener();
            }
            return true;
        }
        return false;
    }

    public interface OnKeyBackListener {
        void onKeyBackListener();
    }

    class DialogView extends LinearLayout {

        private static final int INDEX0 = 0, INDEX1 = 1;

        private ProgressBar mProgressBar;

        private TextView mTextView;

        public DialogView(Context context) {
            super(context);
            initView();
            initProgressBar();
            initTextView();
        }

        private void initView() {
            this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            this.setPadding(padding10, padding10, padding10, padding10);
            this.setGravity(Gravity.CENTER);
            this.setOrientation(LinearLayout.VERTICAL);
        }

        private void initProgressBar() {
            if (mProgressBar == null) {
                mProgressBar = new ProgressBar(mContext);
            }
            if (this.getChildAt(INDEX0) == null) {
                LayoutParams params = new LayoutParams(
                        width / 2, height / 2);
                params.gravity = Gravity.CENTER;
                this.addView(mProgressBar, INDEX0, params);
            }
        }

        private void initTextView() {
            if (mTextView == null) {
                mTextView = new TextView(mContext);
            }
            if (this.getChildAt(INDEX1) == null) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                params.setMargins(0, ZFormat.dip2px(mContext, 10), 0, 0);
                mTextView.setGravity(Gravity.CENTER);
                mTextView.setTextColor(TEXT_COLOR);
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                mTextView.setSingleLine(true);
                mTextView.setEllipsize(TruncateAt.END);
                this.addView(mTextView, INDEX1, params);
            }
        }

        public void setText(String text) {
            if (mTextView == null) {
                initTextView();
            }
            mTextView.setText(text);
        }

    }

}
