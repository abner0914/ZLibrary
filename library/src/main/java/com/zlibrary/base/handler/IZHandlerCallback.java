package com.zlibrary.base.handler;

import com.ZLibrary.base.entity.ZMessage;

public interface IZHandlerCallback {

    /**
     * 返回成功结果，运行在UI线程
     *
     * @param msg       请求结果处理后的实体封装
     * @param requestId 请求ID
     */
    void onResultSuccess(ZMessage msg, int requestId);

    /**
     * 返回失败结果，运行在UI线程
     *
     * @param msg
     * @param requestId
     */
    void onResultFail(ZMessage msg, int requestId);

}
