package com.zlibrary.base.viewPagerIndicator.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class LazyFragment extends LazyBaseFragment {
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
    //未设置值
    private static final int VISIBLE_STATE_NOTSET = -1;
    //可见
    private static final int VISIBLE_STATE_VISIABLE = 1;
    //不可见
    private static final int VISIBLE_STATE_GONE = 0;
    private boolean isInit = false;
    private Bundle savedInstanceState;
    private boolean isLazyLoad = true;
    private FrameLayout layout;
    private int isVisibleToUserState = VISIBLE_STATE_NOTSET;
    private boolean isStart = false;

    @Deprecated
    protected final void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        }
        boolean isVisibleToUser;
        if (isVisibleToUserState == VISIBLE_STATE_NOTSET) {
            isVisibleToUser = getUserVisibleHint();
        } else {
            isVisibleToUser = isVisibleToUserState == VISIBLE_STATE_VISIABLE;
        }
        if (isLazyLoad) {
            if (isVisibleToUser && !isInit) {
                isInit = true;
                onCreateViewLazy(savedInstanceState);
            } else {
                LayoutInflater layoutInflater = inflater;
                if (layoutInflater == null) {
                    layoutInflater = LayoutInflater.from(getApplicationContext());
                }
                layout = new FrameLayout(layoutInflater.getContext());
                View view = getPreviewLayout(layoutInflater, layout);
                if (view != null) {
                    layout.addView(view);
                }
                layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                super.setContentView(layout);
            }
        } else {
            isInit = true;
            onCreateViewLazy(savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisibleToUserState = isVisibleToUser ? VISIBLE_STATE_VISIABLE : VISIBLE_STATE_GONE;
        if (isVisibleToUser && !isInit && getContentView() != null) {
            isInit = true;
            onCreateViewLazy(savedInstanceState);
            onResumeLazy();
        }
        if (isInit && getContentView() != null) {
            if (isVisibleToUser) {
                isStart = true;
                onFragmentStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }
    }

    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Deprecated
    @Override
    public final void onStart() {
        super.onStart();
        if (isInit && !isStart && getUserVisibleHint()) {
            isStart = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    protected void onFragmentStartLazy() {

    }

    protected void onFragmentStopLazy() {

    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {

    }

    protected void onResumeLazy() {

    }

    protected void onPauseLazy() {

    }

    protected void onDestroyViewLazy() {

    }

    @Override
    public void setContentView(int layoutResID) {
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            View view = inflater.inflate(layoutResID, layout, false);
            layout.addView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            layout.addView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    @Deprecated
    public final void onResume() {
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
    }
}
