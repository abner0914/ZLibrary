package com.zlibrary.base.net;

import android.util.SparseArray;

import com.ZLibrary.base.entity.ZReqEntity;
import com.ZLibrary.base.util.ZL;

public abstract class ZNetwork implements IZNetwork {

    /**
     * 存放当前所有线程的集合
     */
    private SparseArray<ZRequest> mRequestThreads;
    private SparseArray<ZDownload> mDownloadThreads;

    /**
     * 构造函数
     */
    public ZNetwork() {
        mRequestThreads = new SparseArray<ZRequest>();
        mDownloadThreads = new SparseArray<ZDownload>();
    }

    @Override
    public void request(ZReqEntity reqEntity, int requestId,
                        IZNetworkCallback callback) {
        if (reqEntity == null) {
            throw new NullPointerException(
                    "The network requests the LReqEntity parameter cannot be empty!");
        }
        if (callback == null) {
            throw new NullPointerException(
                    "This is an invalid request,because you did not realize the callback interface!");
        }
        ZRequest task = mRequestThreads.get(requestId);
        if (task == null || task.getState() == LReqState.FINISHED) {
            task = new ZRequest(reqEntity, requestId, callback, this);
            task.execute();
            mRequestThreads.put(requestId, task);
        } else {
            ZL.i(TAG, "requestId " + requestId + " thread is running!");
        }
    }

    @Override
    public void stopAllThread() {
        for (int index = 0, size = mRequestThreads.size(); index < size; index++) {
            int key = mRequestThreads.keyAt(index);
            stopRequestThread(key);
        }
        for (int index = 0, size = mDownloadThreads.size(); index < size; index++) {
            int key = mDownloadThreads.keyAt(index);
            stopDownloadThread(key);
        }
    }

    @Override
    public void stopRequestThread(int requestId) {
        ZRequest task = mRequestThreads.get(requestId);
        if (task != null) {
            task.stop();
        }
    }

    @Override
    public void stopDownloadThread(int requestId) {
        ZDownload task = mDownloadThreads.get(requestId);
        if (task != null) {
            task.stop();
        }
    }

    @Override
    public void download(String url, String savePath, String saveName,
                         int requestId, IZNetworkCallback callback) {
        ZDownload task = mDownloadThreads.get(requestId);
        if (task == null || task.getState() == LReqState.FINISHED) {
            task = new ZDownload(url, savePath, saveName, requestId, callback,
                    this);
            task.execute();
            mDownloadThreads.put(requestId, task);
        } else {
            ZL.i(TAG, "requestId " + requestId + " thread is running!");
        }
    }

    /**
     * 线程状态
     *
     * @author object
     */
    enum LReqState {

        /**
         * 线程状态为等待执行
         */
        PENDING,

        /**
         * 线程状态为正在执行
         */
        RUNNING,

        /**
         * 线程状态为执行结束
         */
        FINISHED
    }

}
