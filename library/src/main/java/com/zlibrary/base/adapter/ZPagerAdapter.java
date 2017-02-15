package com.zlibrary.base.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class ZPagerAdapter<T> extends PagerAdapter {

    private ZAdapters<T> mAdapter;

    private SparseArray<View> mViewList;

    public ZPagerAdapter(Context context) {
        initAdapters(context, null);
    }

    public ZPagerAdapter(Context context, List<T> list) {
        initAdapters(context, list);
    }

    /**
     * 构造函数
     *
     * @param context 传入上下文对象
     * @param list    传入T类型的{@link List}
     */
    private void initAdapters(Context context, List<T> list) {
        mAdapter = new ZAdapters<T>(context, list);
        this.mViewList = new SparseArray<View>();
    }

    public ZAdapters<T> getAdapter() {
        return mAdapter;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mAdapter != null) {
            count = mAdapter.getCount();
        }
        return count;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mViewList == null || mViewList.size() <= 0 || position < 0
                || mViewList.size() <= position) {
            return;
        }
        View view = mViewList.get(position);
        if (view == null) {
            return;
        }
        container.removeView(view);
        mViewList.remove(position);
    }

    /**
     * 添加页面样式
     */
    @Override
    public abstract Object instantiateItem(ViewGroup container, int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 清除View
     */
    public void clearView() {
        if (mViewList != null) {
            mViewList.clear();
        }
        this.notifyDataSetChanged();
    }

    /**
     * 销毁方法
     */
    public void destroy() {
        clearView();
        mViewList = null;
        if (mAdapter != null) {
            mAdapter.destroyList();
            mAdapter.setList(null);
        }
    }

    public SparseArray<View> getViewList() {
        return mViewList;
    }

}
