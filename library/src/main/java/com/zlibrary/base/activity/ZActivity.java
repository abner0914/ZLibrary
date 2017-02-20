package com.zlibrary.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zlibrary.base.application.ZApplication;
import com.zlibrary.base.dialog.ZProgress;
import com.zlibrary.base.dialog.ZProgress.OnKeyBackListener;
import com.zlibrary.base.entity.ZMessage;
import com.zlibrary.base.handler.IZHandlerCallback;

/**
 * 基础 Activity
 */
public abstract class ZActivity extends FragmentActivity implements
        IZHandlerCallback, OnKeyBackListener {

    public Context mContext;

    public ZApplication mLApplication;

    private ZProgress mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mLApplication = ZApplication.getInstance();
        mLApplication.setContext(mContext);
        mLApplication.setDestroyActivitys(false);
        mLApplication.addActivity(this);
        mLApplication.setFragmentManager(this.getSupportFragmentManager());
        onZCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
        mProgressDialog = null;
    }

    @Override
    protected void onDestroy() {
        mLApplication.delActivity(this);
        super.onDestroy();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity
     *
     * @param action
     */
    protected void readyGo(String action) {
        Intent intent = new Intent(action);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param action
     * @param bundle
     */
    protected void readyGo(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param action
     */
    protected void readyGoThenKill(String action) {
        Intent intent = new Intent(action);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param action
     * @param bundle
     */
    protected void readyGoThenKill(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult
     *
     * @param action
     * @param requestCode
     */
    protected void readyGoForResult(String action, int requestCode) {
        Intent intent = new Intent(action);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param action
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(String action, int requestCode, Bundle bundle) {
        Intent intent = new Intent(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    protected void showProgressDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new ZProgress(this);
            mProgressDialog.setOnKeyBackListener(this);
        }
        mProgressDialog.show(text);
    }


    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onKeyBackListener() {
    }

    protected abstract void onZCreate(Bundle savedInstanceState);


    @Override
    public void onResultSuccess(ZMessage msg, int requestId) {
    }

    @Override
    public void onResultFail(ZMessage msg, int requestId) {
    }
}
