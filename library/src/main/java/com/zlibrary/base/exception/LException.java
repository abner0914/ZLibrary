package com.zlibrary.base.exception;

import com.zlibrary.base.application.ZApplication;
import com.zlibrary.base.util.ZL;


public abstract class LException extends Exception implements IZException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 获取基本异常信息
     *
     * @param e
     * @return
     */
    public static String getStackMsg(Throwable e) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        sb.append(e);
        return sb.toString();
    }

    public void printException(Exception e) {
        if (ZApplication.getInstance().getIsOpenDebugMode()) {
            ZL.e(LException.getStackMsg(e));
        }
    }

    public void printException(Exception e, boolean isCapture) {
        printException(e);
        if (isCapture) {
            captureException(e);
        }
    }

    @Override
    public abstract void captureException(Exception e);

    @Override
    public abstract void handleAccomplish(LExcState state);

}
