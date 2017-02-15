package com.zlibrary.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zlibrary.base.cache.ZCache;
import com.zlibrary.base.entity.ZMessage;
import com.zlibrary.base.handler.IZHandlerCallback;
import com.zlibrary.base.handler.ZHandler;

import java.util.List;


public abstract class ZBaseAdapter<T> extends BaseAdapter implements IZHandlerCallback {

    public Context mContext;
    /**
     * 缓存View对象
     */
    private ZCache<View> cacheView = new ZCache<View>();
    private ZAdapters<T> mAdapter;
    private LayoutInflater mInflater;

    /**
     * 构造函数
     *
     * @param context 传入上下文对象
     */
    public ZBaseAdapter(Context context) {
        initAdapter(context, null);
    }

    /**
     * 构造函数
     *
     * @param context 传入上下文对象
     * @param list    数据集合
     */
    public ZBaseAdapter(Context context, List<T> list) {
        initAdapter(context, list);
    }

    /**
     * 初始化
     *
     * @param context
     * @param list
     */
    private void initAdapter(Context context, List<T> list) {
        mContext = context;
        mAdapter = new ZAdapters<T>(context, list);
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
    public Object getItem(int position) {
        Object o = null;
        if (mAdapter != null) {
            o = mAdapter.getItem(position);
        }
        return o;
    }

    @Override
    public long getItemId(int position) {
        long p = 0;
        if (mAdapter != null) {
            p = mAdapter.getItemId(position);
        }
        return p;
    }

    /**
     * 抽象的getView<br/>
     * 系统会自动调用getView方法来获取List数据的每一条显示内容<br/>
     * 需要手动实现此方法
     */
    @Override
    public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

    /**
     * 获取一个LayoutInflater实例
     *
     * @return
     */
    public LayoutInflater getInflater() {
        if (mInflater == null) {
            mInflater = (LayoutInflater) this.getAdapter().getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    /**
     * 清除缓存数据，并不刷新UI
     */
    public void destroyConvertView() {
        if (cacheView != null)
            cacheView.clearAll();
    }

    /**
     * 清除内存数据，并不刷新UI
     */
    public void Destroy() {
        destroyConvertView();
        cacheView = null;
        if (mAdapter != null) {
            mAdapter.destroyList();
            mAdapter.setList(null);
        }
    }

    /**
     * 添加缓存view
     *
     * @param key
     * @param value
     */
    public void addConvertView(String key, View value) {
        if (!cacheView.isHaveCache(key)) {
            cacheView.put(key, value);
        }
    }

    /**
     * 删除缓存view
     *
     * @param key
     */
    public void delConvertView(String key) {
        if (cacheView.isHaveCache(key)) {
            cacheView.remove(key);
        }
    }

    /**
     * 查询缓存view
     *
     * @param key
     * @return
     */
    public View getConvertView(String key) {
        View v = null;
        if (cacheView.isHaveCache(key)) {
            v = cacheView.get(key);
        }
        return v;
    }

    /**
     * 清除所有缓存并刷新UI
     */
    @Override
    public void notifyDataSetChanged() {
        destroyConvertView();
        super.notifyDataSetChanged();
    }

    /**
     * 清除指定KEY的缓存并刷新UI
     *
     * @param key ：需要重新生成UI的KEY
     */
    public void notifyDataSetChanged(String key) {
        delConvertView(key);
        super.notifyDataSetChanged();
    }

    /**
     * 当你使用了
     * LHandler.startLoadingData(LReqEntity)} 方法请求网络后，
     * {@linkplain ZHandler LHandler} 会自动调用此方法，并将解析的结果
     * {@linkplain ZMessage LMessage} 对象传回
     *
     * @param msg
     */
    public void onResultSuccess(ZMessage msg, int requestId) {
        // ... 写入你需要的代码
    }

    @Override
    public void onResultFail(ZMessage msg, int requestId) {
        // TODO Auto-generated method stub

    }

}
