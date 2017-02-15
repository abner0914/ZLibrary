package com.zlibrary.base.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ZLibrary.base.application.ZApplication;
import com.ZLibrary.base.widget.webview.ZWebViewClient;

import java.util.Map;

/**
 * 自定义webview 简单实现了session控制
 */
public class ZWebView extends WebView {

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 请求路径
     */
    private String mUrl;

    /**
     * 额外的标头
     */
    private Map<String, String> mExtraHeaders;

    /**
     * WebViewClient子类
     */
    private ZWebViewClient mWebViewClient;

    public ZWebView(Context context) {
        super(context);
        init(context);
    }

    public ZWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.setWebViewClient(getWebViewClient());
    }

    @Override
    public void loadUrl(String url) {
        mUrl = url;
        mExtraHeaders = null;
        initCookie();
        new RequestAsyncTask().execute();
    }

    /**
     * 开始加载
     */
    public void loadUrl(String url, Map<String, String> extraHeaders) {
        mUrl = url;
        mExtraHeaders = extraHeaders;
        initCookie();
        new RequestAsyncTask().execute();

    }

    /**
     * 初始化CookieManager
     */
    private void initCookie() {
        CookieSyncManager.createInstance(mContext);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().removeSessionCookie();
    }

    /**
     * 同步CookieManager
     */
    private void syncCookie() {
        ZApplication.getInstance().setSessionValue("123456789");
        CookieManager.getInstance().setCookie(
                mUrl,
                ZApplication.getInstance().getSessionKey() + "="
                        + ZApplication.getInstance().getSessionValue());
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 加载
     */
    private void myLoadUrl() {
        if (Build.VERSION.SDK_INT >= 8) {
            super.loadUrl(mUrl, mExtraHeaders);
        } else {
            super.loadUrl(mUrl);
        }
    }

    private WebViewClient getWebViewClient() {
        if (mWebViewClient == null) {
            mWebViewClient = new ZWebViewClient();
        }
        return mWebViewClient;
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        mWebViewClient = (ZWebViewClient) client;
        super.setWebViewClient(mWebViewClient);
    }

    private class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(20);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            syncCookie();
            myLoadUrl();
        }
    }

}
