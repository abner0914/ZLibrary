package com.zlibrary.base.adapter;

import android.content.Context;

import com.zlibrary.base.util.ZList;

import java.util.List;


public final class ZAdapters<T> {

    private Context mContext;

    private List<T> mList;

    public ZAdapters(Context context) {
        this.mContext = context;
    }


    public ZAdapters(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }


    public Context getContext() {
        return mContext;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }

    public int getCount() {
        int count = 0;
        if (mList != null)
            count = mList.size();
        return count;
    }

    public Object getItem(int position) {
        Object o = null;
        if (mList != null && position < mList.size())
            o = mList.get(position);
        return o;
    }


    public long getItemId(int position) {
        return position;
    }


    public void destroyList() {
        if (mList != null)
            mList.clear();
    }

    public void addDataFirst(T data) {
        if (data != null)
            mList.add(0, data);
    }

    public void addDataIndex(int index, T data) {
        if (data != null && !ZList.isEmpty(mList))
            mList.add(index, data);
    }

    public void addDataLast(T data) {
        if (data != null)
            mList.add(data);
    }

    public void addDatasToFirst(List<T> datas) {
        if (datas != null) {
            for (T t : datas) {
                mList.add(0, t);
            }
        }
    }


    public void addDatasToLast(List<T> datas) {
        if (datas != null) {
            for (T t : datas) {
                mList.add(t);
            }
        }
    }

    public void delData(T data) {
        mList.remove(data);
    }

    public void delData(int position) {
        mList.remove(position);
    }

}
