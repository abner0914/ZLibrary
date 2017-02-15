package com.zlibrary.base.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ZLibrary.base.activity.ZActivity;
import com.ZLibrary.base.dialog.ZProgress;
import com.ZLibrary.base.dialog.ZProgress.OnKeyBackListener;
import com.ZLibrary.base.entity.ZMessage;
import com.ZLibrary.base.handler.IZHandlerCallback;

public abstract class ZFragment extends Fragment implements IZHandlerCallback, OnKeyBackListener {

    protected ZActivity mActivity;

    protected View mView;

    private ZProgress mProgressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (ZActivity) activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissProgressDialog();
        mProgressDialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void showProgressDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new ZProgress(mActivity);
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


    @Override
    public void onResultSuccess(ZMessage msg, int requestId) {
    }

    @Override
    public void onResultFail(ZMessage msg, int requestId) {

    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);
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
        Intent intent = new Intent(mActivity, clazz);
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
        mActivity.finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        mActivity.finish();
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
        mActivity.finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(mActivity, clazz);
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
        Intent intent = new Intent(mActivity, clazz);
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

}
