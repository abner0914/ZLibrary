package com.zlibrary.base.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.zlibrary.base.application.ZApplication;

/**
 * 手机信息获取
 */
public class ZMobileInfo {

    /**
     * 获取IMEI码
     *
     * @return
     */
    public static String getImei() {
        TelephonyManager mTm = (TelephonyManager) ZApplication.getInstance()
                .getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTm.getDeviceId();
    }

    /**
     * 获取IMSI码
     *
     * @return
     */
    public static String getImsi() {
        TelephonyManager mTm = (TelephonyManager) ZApplication.getInstance()
                .getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTm.getSubscriberId();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileType() {
        return android.os.Build.MODEL; // 手机型号
    }

    /**
     * 获取系统发布版本号
     *
     * @return
     */

    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * 获取手机信息
     */
    public static String printTelephoneInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------  手机信息  --------------------");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        //IMSI前面三位460是国家号码，其次的两位是运营商代号，00、02是中国移动，01是联通，03是电信。
        String providerName = null;
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providerName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                providerName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                providerName = "中国电信";
            }
        }
        sb.append(providerName + "  手机号：" + tm.getLine1Number() + " IMSI是：" + IMSI);
        sb.append("\nDeviceID(IMEI)       :" + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion:" + tm.getDeviceSoftwareVersion());
        sb.append("\ngetLine1Number       :" + tm.getLine1Number());
        sb.append("\nNetworkCountryIso    :" + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator      :" + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName  :" + tm.getNetworkOperatorName());
        sb.append("\nNetworkType          :" + tm.getNetworkType());
        sb.append("\nPhoneType            :" + tm.getPhoneType());
        sb.append("\nSimCountryIso        :" + tm.getSimCountryIso());
        sb.append("\nSimOperator          :" + tm.getSimOperator());
        sb.append("\nSimOperatorName      :" + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber      :" + tm.getSimSerialNumber());
        sb.append("\ngetSimState          :" + tm.getSimState());
        sb.append("\nSubscriberId         :" + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber      :" + tm.getVoiceMailNumber());
        return sb.toString();

    }

}
