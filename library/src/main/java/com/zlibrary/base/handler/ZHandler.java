package com.zlibrary.base.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ZLibrary.base.activity.ZActivity;
import com.ZLibrary.base.activity.fragment.ZFragment;
import com.ZLibrary.base.adapter.ZBaseAdapter;
import com.ZLibrary.base.application.ZApplication;
import com.ZLibrary.base.entity.ZMessage;
import com.ZLibrary.base.entity.ZReqEntity;
import com.ZLibrary.base.net.IZNetwork;
import com.ZLibrary.base.net.IZNetworkCallback;
import com.ZLibrary.base.service.ZService;
import com.ZLibrary.base.util.ZFormat;


public abstract class ZHandler extends Handler implements IZNetworkCallback {

    /**
     * 默认进动画
     */
    private static final int DEFAULT_ENTER_ANIM = -1;

    /**
     * 默认出动画
     */
    private static final int DEFAULT_EXIT_ANIM = -1;

    /**
     * 默认返回进动画
     */
    private static final int DEFAULT_BACK_ENTER_ANIM = -1;

    /**
     * 默认返回出动画
     */
    private static final int DEFAULT_BACK_EXIT_ANIM = -1;

    /**
     * 当前Activity对象
     */
    private ZActivity mActivity;

    /**
     * 当前Fragment对象
     */
    private ZFragment mFragment;

    /**
     * 当前BaseAdapter对象
     */
    private ZBaseAdapter<?> mBaseAdapter;

    /**
     * 当前Service对象
     */
    private ZService mService;

    /**
     * 设置LNetwork监听
     */
    private IZNetwork mNetworkListener;

    /**
     * 返回结果回调接口，用于通知所对应的LActivity、ZFragment、或LBaseAdapter
     */
    private IZHandlerCallback mILHandlerCallback;

    /**
     * 构造函数
     */
    public ZHandler() {

    }

    /**
     * 构造函数
     *
     * @param activity
     */
    public ZHandler(ZActivity activity) {
        this.mActivity = activity;
        try {
            this.mILHandlerCallback = activity;
        } catch (ClassCastException e) {
            this.mILHandlerCallback = null;
        }
    }

    /**
     * 构造函数
     *
     * @param fragment
     */
    public ZHandler(ZFragment fragment) {
        this.mFragment = fragment;
        try {
            this.mILHandlerCallback = mFragment;
        } catch (ClassCastException e) {
            this.mILHandlerCallback = null;
        }
    }

    /**
     * 构造函数
     *
     * @param <T>
     * @param mBaseAdapter
     */
    public <T> ZHandler(ZBaseAdapter<T> mBaseAdapter) {
        this.mBaseAdapter = mBaseAdapter;
        try {
            this.mILHandlerCallback = mBaseAdapter;
        } catch (ClassCastException e) {
            this.mILHandlerCallback = null;
        }
    }

    public ZHandler(ZService service) {
        this.mService = service;
        try {
            this.mILHandlerCallback = mService;
        } catch (ClassCastException e) {
            this.mILHandlerCallback = null;
        }
    }

    /**
     * 初始化网络请求监听
     *
     * @param network
     */
    public void initNetwork(IZNetwork network) {
        mNetworkListener = network;
        if (mNetworkListener != null) {
        } else {
            throw new NullPointerException(
                    "The LNetwork is null, you must changed setLNetworkListener(LNetwork)");
        }
    }

    /**
     * 设置返回结果回调接口，用于通知所对应的LActivity、ZFragment、或LBaseAdapter
     *
     * @param calback
     */
    public void setILHandlerCallback(IZHandlerCallback calback) {
        this.mILHandlerCallback = calback;
    }

    /**
     * 获取返回结果回调接口
     *
     * @return
     */
    public IZHandlerCallback getCallback() {
        return mILHandlerCallback;
    }

    /**
     * 开始请求网络连接
     *
     * @param entity ：网络请求所需要的参数对象
     */
    public void request(ZReqEntity entity) {
        request(entity, 0);
    }

    /**
     * 重写此方法，操作基本网络连接
     *
     * @param entity    ：网络请求所需要的参数对象
     * @param requestId : 请求ID，方便区分不同请求
     */
    public void request(ZReqEntity entity, int requestId) {
        if (mNetworkListener != null) {
            mNetworkListener.request(entity, requestId, this);
        } else {
            throw new NullPointerException(
                    "The LNetwork is null, you must changed setLNetworkListener(LNetwork)");
        }
    }

    /**
     * 停止当前实体所有线程
     */
    public void stopAllThread() {
        if (mNetworkListener != null) {
            mNetworkListener.stopAllThread();
        }
    }

    /**
     * 停止当前实体requestId所对应的线程
     *
     * @param requestId
     */
    public void stopRequestThread(int requestId) {
        if (mNetworkListener != null) {
            mNetworkListener.stopRequestThread(requestId);
        }
    }

    public void stopDownloadThread(int requestId) {
        if (mNetworkListener != null) {
            mNetworkListener.stopDownloadThread(requestId);
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param savePath
     * @param saveName
     */
    public void download(String url, String savePath, String saveName,
                         int requestId) {
        if (ZFormat.isEmpty(url)) {
            throw new IllegalArgumentException("The URL cannot be empty!");
        }
        if (ZFormat.isEmpty(savePath)) {
            savePath = ZApplication.getInstance().getCacheFile().getPath();
        }
        if (ZFormat.isEmpty(saveName)) {
            saveName = ZFormat.getMD5Url(url);
        }
        if (mNetworkListener != null) {
            mNetworkListener.download(url, savePath, saveName, requestId, this);
        } else {
            throw new NullPointerException(
                    "The LNetwork is null, you must changed setLNetworkListener(LNetwork)");
        }
    }

    /**
     * 返回请求解析的结果<br/>
     * 自动调用发出请求view的onResultHandler方法，使用者请自主复写此方法，以便处理<br/>
     */
    @Override
    public void onHandlerUI(ZMessage result, int requestId) {
        IZHandlerCallback callback = getCallback();
        if (callback != null) {
            callback.onResultSuccess(result, requestId);
        }
    }

    /**
     * 注销当前Activity
     */
    public void finishActivity() {
        if (mActivity != null) {
            mActivity.finish();
            mActivity.overridePendingTransition(DEFAULT_BACK_ENTER_ANIM,
                    DEFAULT_BACK_EXIT_ANIM);
        }
    }

    /**
     * 跳转Activity
     *
     * @param cls ：需要跳转到的Activity
     */
    public void jumpActivity(Class<?> cls) {
        jumpActivity(cls, null, false, DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM);
    }

    /**
     * 跳转Activity
     *
     * @param cls      ：需要跳转到的Activity
     * @param isFinish ：是否注销此Activity
     */
    public void jumpActivity(Class<?> cls, boolean isFinish) {
        jumpActivity(cls, null, isFinish, DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM);
    }

    /**
     * 跳转Activity
     *
     * @param cls  ：需要跳转到的Activity
     * @param data ：需要传递的参数
     */
    public void jumpActivity(Class<?> cls, Bundle data) {
        jumpActivity(cls, data, false, DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM);
    }

    /**
     * 跳转Activity
     *
     * @param cls      ：需要跳转到的Activity
     * @param data     ：需要传递的参数
     * @param isFinish ：是否注销此Activity
     */
    public void jumpActivity(Class<?> cls, Bundle data, boolean isFinish) {
        jumpActivity(cls, data, isFinish, DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM);
    }

    /**
     * 跳转Activity
     *
     * @param cls       ：需要跳转到的Activity
     * @param data      ：需要传递的参数
     * @param isFinish  ：是否注销此Activity
     * @param enterAnim ：进入动画
     * @param exitAnim  ：送出动画
     */
    public void jumpActivity(Class<?> cls, Bundle data, boolean isFinish,
                             int enterAnim, int exitAnim) {
        ZActivity context = null;
        if (mFragment != null) {
            context = (ZActivity) mFragment.getActivity();
        } else if (mActivity != null) {
            context = mActivity;
        } else if (mBaseAdapter != null) {
            context = (ZActivity) mBaseAdapter.getAdapter().getContext();
        }
        if (context != null) {
            Intent intent = new Intent(context, cls);
            if (data != null) {
                intent.putExtras(data);
            }
            context.startActivity(intent);
            if (isFinish) {
                finishActivity();
            }
            // API LEVEL5.0
            context.overridePendingTransition(enterAnim, exitAnim);
        }
    }
}
