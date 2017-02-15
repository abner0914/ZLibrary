package com.zlibrary.base.net;


public interface IZNetworkProgress {

    /**
     * 上传/下载文件发送进度，运行在子线程，不可直接操控UI
     *
     * @param count   总长度
     * @param current 当前长度
     */
    void sendProgress(int count, int current);

}
