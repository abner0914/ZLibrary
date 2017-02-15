package com.zlibrary.base.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zlibrary.base.application.ZApplication;

import java.util.List;


/**
 * 设备辅助连接工具类
 */
public class ZDiscConn {

    private ZDiscConn() {
    }

    /**
     * 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
     * <p/>
     * true已打开 false未打开
     */
    public static boolean IsConnNet() {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) ZApplication
                .getInstance().getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conManager != null) {
            NetworkInfo network = conManager.getActiveNetworkInfo();
            if (network != null) {
                bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
            }
        }
        return bisConnFlag;
    }

    /**
     * 判断GPS是否打开
     */
    public static boolean IsGPSEnabled() {
        LocationManager locationManager = (LocationManager) ZApplication
                .getInstance().getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断所用机型是否有GPS设备模块
     */
    public static boolean HasGPSDevice() {
        LocationManager mgr = (LocationManager) ZApplication.getInstance()
                .getContext().getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }
}
