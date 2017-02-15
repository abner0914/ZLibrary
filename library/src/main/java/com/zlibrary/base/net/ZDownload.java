package com.zlibrary.base.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zlibrary.base.entity.ZMessage;
import com.zlibrary.base.exception.LException;
import com.zlibrary.base.exception.ZLoginException;
import com.zlibrary.base.net.IZNetwork.LLoginState;
import com.zlibrary.base.net.IZNetwork.LReqResultState;
import com.zlibrary.base.net.ZNetwork.LReqState;
import com.zlibrary.base.util.ZL;

import org.json.JSONException;

import java.io.File;

/**
 * 文件下载
 */
public class ZDownload extends AsyncTask<Void, Void, LReqResultState> implements
        IZNetworkProgress {

    private static final String TAG = ZDownload.class.getSimpleName();

    private String mUrl, mSavePath, mSaveName;

    private IZNetworkCallback mCallback;

    private LDownloadStoppingEntity mIsStopThread;

    private int mRequestId;

    private ZMessage mMessage;

    /**
     * 网络请求接口
     */
    private IZNetwork mNetwork;

    /**
     * 线程状态
     */
    private LReqState mThreadState;
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

    public ZDownload(String url, String savePath, String saveName,
                     int requestId, IZNetworkCallback callback, IZNetwork network) {
        mThreadState = LReqState.PENDING;
        this.mUrl = url;
        this.mSavePath = savePath;
        this.mSaveName = saveName;
        this.mCallback = callback;
        this.mRequestId = requestId;
        this.mNetwork = network;
        mIsStopThread = new LDownloadStoppingEntity();
        mIsStopThread.isStopping = false;
    }

    @Override
    protected LReqResultState doInBackground(Void... params) {
        if (mIsStopThread.isStopping)
            return LReqResultState.STOP;
        mThreadState = LReqState.RUNNING;
        String fileUrl = null;
        try {
            fileUrl = ZCaller.doDownloadFile(mUrl, mSavePath, mSaveName, this,
                    mIsStopThread);
        } catch (Exception e) {
            ZL.e(LException.getStackMsg(e));
            return LReqResultState.NETWORK_EXC;
        }
        if (mCallback != null) {
            if (mIsStopThread.isStopping)
                return LReqResultState.STOP;
            File file = new File(fileUrl);
            if (file.exists()) {
                try {
                    mMessage = mCallback.onParse(fileUrl, mRequestId);
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
            }
        } else {
            throw new NullPointerException(
                    "This is an invalid request,because you did not realize the callback interface!");
        }
        return LReqResultState.SUCCESS;
    }

    @Override
    protected void onPostExecute(LReqResultState result) {
        super.onPostExecute(result);
        if (mIsStopThread.isStopping)
            result = LReqResultState.STOP;
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
                    mNetwork.download(mUrl, mSavePath, mSaveName, mRequestId,
                            mCallback);
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

    @Override
    public void sendProgress(int size, int current) {
        Message msg = Message.obtain();
        msg.arg1 = size;
        msg.arg2 = current;
        mHandler.sendMessage(msg);
    }

    public void stop() {
        this.mIsStopThread.isStopping = true;
    }

    public LReqState getState() {
        return mThreadState;
    }

    class LDownloadStoppingEntity {
        boolean isStopping;
    }

}
