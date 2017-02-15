package com.zlibrary.base.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取设备的唯一标识
 */
public class DeviceFactory {

    //cc 1 The IMEI
    //only useful for Android Phone(android.permission.READ_PHONE_STATE in Manifest)
    private static String getIMEI(Context mContext) {
        TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

    //cc 2 Pseudo-Unique ID
    //useful for phone/pad
    //通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）
    private static String getPUID() {
        String m_szDevIDShort = "35" + //make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    //cc 3 Android ID
    //sometimes it will be null,cause this id can be changed by the manufacturer
    private static String getAndroidId(Context mContext) {
        String m_szAndroidID = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    //cc 4 The WLAN MAC Address String
    //need android.permission.ACCESS_WIFI_STATE,or it will return null
    private static String getWLANMAC(Context mContext) {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    //cc 5 the BT MAC Address String
    //need android.permission.BLUETOOTH,or it will return null
    private static String getBTMAC() {
        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (m_BluetoothAdapter != null) {
            return m_BluetoothAdapter.getAddress();
        }
        return "";
    }

    //cc 5 the ro.serialno String
    private static String getSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
            serial = "";
        }
        return serial;
    }

    //cc Combined Device ID
    public static String getCombinedId(Context mContext) {
        String szImei = getIMEI(mContext);
        String m_szDevIDShort = getPUID();
        String m_szAndroidID = getAndroidId(mContext);
//		String m_szWLANMAC=getWLANMAC(mContext);
        String m_szBTMAC = getBTMAC();
        String m_szSerial = getSerial();
        String m_szLongID = szImei + m_szDevIDShort + m_szAndroidID + m_szBTMAC + m_szSerial;
        Log.i("log", "ime_id:" + szImei);
        Log.i("log", "dev_id:" + m_szDevIDShort);
        Log.i("log", "and_id:" + m_szAndroidID);
        Log.i("log", "bt_mac:" + m_szBTMAC);
        Log.i("log", "se_mac:" + getSerial());

        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }

}

