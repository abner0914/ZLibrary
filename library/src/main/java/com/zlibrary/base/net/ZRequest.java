package com.zlibrary.base.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.ZLibrary.base.entity.ZMessage;
import com.ZLibrary.base.entity.ZReqEncode;
import com.ZLibrary.base.entity.ZReqEntity;
import com.ZLibrary.base.entity.ZReqMothed;
import com.ZLibrary.base.entity.ZUploadEntity;
import com.ZLibrary.base.exception.LException;
import com.ZLibrary.base.exception.ZLoginException;
import com.ZLibrary.base.net.IZNetwork.LLoginState;
import com.ZLibrary.base.net.IZNetwork.LReqResultState;
import com.ZLibrary.base.net.ZNetwork.LReqState;
import com.ZLibrary.base.util.ZFormat;
import com.ZLibrary.base.util.ZL;

import org.json.JSONException;

import java.util.Map;

/**
 * 网络请求
 */
public class ZRequest extends AsyncTask<Void, Void, LReqResultState> implements
        IZNetworkProgress {

    private static final String TAG = ZRequest.class.getSimpleName();

    /**
     * 请求封装实体
     */
    private ZReqEntity mReqEntity;

    /**
     * 请求ID
     */
    private int mRequestId;

    /**
     * 请求回调接口
     */
    private IZNetworkCallback mCallback;

    /**
     * 网络请求接口
     */
    private IZNetwork mNetwork;

    /**
     * 请求返回值
     */
    private ZMessage mMessage;

    /**
     * 线程状态
     */
    private LReqState mThreadState;

    /**
     * 是否停止线程，默认false
     */
    private boolean mIsStopThread;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCallback != null) {
                int count = msg.arg1;
                int current = msg.arg2;
                mCallback.onProgress(count, current, mRequestId);
            }
        }

    };

    public ZRequest(ZReqEntity reqEntity, int reqId,
                    IZNetworkCallback callback, IZNetwork network) {
        this.mReqEntity = reqEntity;
        this.mRequestId = reqId;
        this.mCallback = callback;
        this.mNetwork = network;
        mThreadState = LReqState.PENDING;
        mIsStopThread = false;
    }

    @Override
    protected LReqResultState doInBackground(Void... params) {
        mThreadState = LReqState.RUNNING;
        ZReqEntity entity = mReqEntity;
        if (entity == null) {
            throw new NullPointerException(
                    "The network requests the LReqEntity parameter cannot be empty!");
        }
        String netUrl = entity.getUrl();
        if (ZFormat.isEmpty(netUrl)) {
            throw new NullPointerException(
                    "The network requests the URL parameter cannot be empty!");
        }
        ZReqEncode netEncode = entity.getReqEncode();
        ZReqMothed netMothed = entity.getReqMode();
        Map<String, String> netParams = entity.getParams();
        String netBody = entity.getBody();
        ZUploadEntity uploadParams = entity.getUploadParams();
        boolean netUseCache = entity.getUseCache();
        if (mIsStopThread)
            return LReqResultState.STOP;
        String result = null;
        try {
            if (uploadParams == null) {
                result = ZCaller.doConn(netUrl, netBody, netUseCache,
                        netMothed, netEncode);
            } else {
                // TODO 上传文件
                // result = ZCaller.doPostStream(netUrl, uploadParams, netUseCache, encode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LReqResultState.NETWORK_EXC;
        }
        if (mCallback != null) {
            if (mIsStopThread)
                return LReqResultState.STOP;
            try {
                mMessage = mCallback.onParse(result, mRequestId);
            } catch (ZLoginException e) {
                mMessage = null;
                // ... 登录处理
                LLoginState loginState = mNetwork.doLogin();
                if (loginState == LLoginState.SUCCESS) {
                    return LReqResultState.LOGIN_SUCCESS;
                } else if (loginState == LLoginState.ERROR) {
                    return LReqResultState.LOGIN_ERROR;
                } else if (loginState == LLoginState.NONE) {
                    return LReqResultState.LOGIN_NONE;
                } else {
                    return LReqResultState.LOGIN_EXC;
                }
            } catch (JSONException e) {
                mMessage = null;
                ZL.e(TAG, LException.getStackMsg(e));
                return LReqResultState.PARSE_EXC;
            } catch (Exception e) {
                mMessage = null;
                ZL.e(TAG, LException.getStackMsg(e));
                return LReqResultState.OTHER;
            }
        } else {
            throw new NullPointerException(
                    "This is an invalid request,because you did not realize the callback interface!");
        }
        return LReqResultState.SUCCESS;
    }

    @Override
    protected void onPostExecute(LReqResultState result) {
        if (mIsStopThread)
            result = LReqResultState.STOP;
        super.onPostExecute(result);
        if (mCallback != null) {
            switch (result) {
                case SUCCESS:
                    mCallback.onHandlerUI(mMessage, mRequestId);
                    break;
                case NETWORK_EXC:
                    mCallback.onException(LReqResultState.NETWORK_EXC, mRequestId);
                    break;
                case PARSE_EXC:
                    mCallback.onException(LReqResultState.PARSE_EXC, mRequestId);
                    break;
                case LOGIN_SUCCESS:
                    ZL.i(TAG, "用户自动登录成功");
                    mThreadState = LReqState.FINISHED;
                    mNetwork.request(mReqEntity, mRequestId, mCallback);
                    break;
                case LOGIN_ERROR:
                    mCallback.onException(LReqResultState.LOGIN_ERROR, mRequestId);
                    break;
                case LOGIN_NONE:
                    mCallback.onException(LReqResultState.LOGIN_NONE, mRequestId);
                    break;
                case LOGIN_EXC:
                    throw new RuntimeException("用户登录返回异常");
                case OTHER:
                    mCallback.onException(LReqResultState.OTHER, mRequestId);
                    break;
                case STOP:
                    ZL.i(TAG, "线程ID为：" + mRequestId + "的线程已停止!");
                    mCallback.onException(LReqResultState.STOP, mRequestId);
                    break;
                default:
                    throw new IllegalArgumentException("返回结果参数错误");
            }
        }
        mThreadState = LReqState.FINISHED;
    }

    /**
     * 发布进度
     *
     * @param count
     * @param current
     */
    @Override
    public void sendProgress(int count, int current) {
        Message msg = Message.obtain();
        msg.arg1 = count;
        msg.arg2 = current;
        mHandler.sendMessage(msg);
    }

    public LReqState getState() {
        return mThreadState;
    }

    public void stop() {
        this.mIsStopThread = true;
        ZL.i("下载是为停止为：" + mIsStopThread);
    }

}
