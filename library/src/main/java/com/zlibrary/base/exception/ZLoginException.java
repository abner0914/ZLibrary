package com.zlibrary.base.exception;

import com.zlibrary.base.util.ZL;

/**
 * 如网络请求时，需要后台登录，则抛出此异常<br/>
 * throw new ZLoginException();
 */
public class ZLoginException extends LException {

    private static final long serialVersionUID = 1L;

    @Override
    public void captureException(Exception e) {
        ZL.e("用户未登录");
    }

    @Override
    public void handleAccomplish(LExcState state) {

    }

}
