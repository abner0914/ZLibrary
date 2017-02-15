package com.zlibrary.base.net;

import com.zlibrary.base.entity.ZMessage;
import com.zlibrary.base.exception.ZLoginException;
import com.zlibrary.base.net.IZNetwork.LReqResultState;

import org.json.JSONException;


public interface IZNetworkCallback {

    /**
     * 请求发现异常，运行于UI线程
     *
     * @param state     ：请求异常状态
     * @param requestId ：请求ID
     */
    void onException(LReqResultState state, int requestId);

    /**
     * 网络请求结果返回，在此处解析返回结果，运行于子线程，不可操作UI
     *
     * @param result    ：请求返回的值
     * @param requestId ：请求ID
     * @throws JSONException   ：如解析错误，向上抛出此异常
     * @throws ZLoginException ：如发现用户在后台未登录，向上抛出此异常
     * @throws Exception       ：其它异常
     * @return：将解析好的数据存放到LMessage中封装
     */
    ZMessage onParse(String strs, int requestId) throws
            Exception;

    /**
     * 请求返回结果，运行于UI线程
     *
     * @param msg       ：返回结果封装
     * @param requestId ：请求ID
     */
    void onHandlerUI(ZMessage msg, int requestId);

    /**
     * 进度控制
     *
     * @param count   总长度
     * @param current 当前长度
     */
    void onProgress(int count, int current, int requestId);

}
