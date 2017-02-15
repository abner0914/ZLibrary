package com.zlibrary.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zlibrary.base.entity.ZMessage;
import com.zlibrary.base.handler.IZHandlerCallback;

/**
 * service 封装
 */
public class ZService extends Service implements IZHandlerCallback {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResultSuccess(ZMessage msg, int requestId) {
        // ... 写入你需要的代码
    }

    @Override
    public void onResultFail(ZMessage msg, int requestId) {
        // TODO Auto-generated method stub

    }

}
