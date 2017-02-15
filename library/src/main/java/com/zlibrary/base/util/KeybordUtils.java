package com.zlibrary.base.util;


import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘操作工具类
 */
public class KeybordUtils {

    /**
     * 是否影藏输入法
     */
    public static void changeKeyBordState(Context mContext, EditText mEditText, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.showSoftInputFromInputMethod(mEditText.getWindowToken(), 0);
        } else {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }
}
