package com.zlibrary.base.widget.webview;

import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ZLibrary.base.application.ZApplication;
import com.ZLibrary.base.util.ZFormat;
import com.ZLibrary.base.util.ZL;

public class ZWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String cookie = CookieManager.getInstance().getCookie(url);
        if (!ZFormat.isEmpty(cookie)) {
            String[] cookies = cookie.split(";");
            if (cookies != null && cookies.length > 0) {
                for (String str : cookies) {
                    int index = str.indexOf(ZApplication.getInstance()
                            .getSessionKey());
                    if (index != -1) {
                        String sessionValue = str.substring(
                                str.indexOf("=") + 1, str.length());
                        ZApplication.getInstance()
                                .setSessionValue(sessionValue);
                        ZL.e(sessionValue);
                    }
                }
            }
        }
    }

}
