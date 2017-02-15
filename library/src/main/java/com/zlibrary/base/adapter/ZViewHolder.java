package com.zlibrary.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZLibrary.base.application.ZApplication;

/**
 * 类描述：通用的ViewHolder
 */
public class ZViewHolder {
    /**
     * 存储item中所用控件引用的容器
     * <p/>
     * Key - 资源ID
     * Value - 控件的引用
     */
    private SparseArray<View> views = null;

    private View convertView = null;

    private int position = 0;

    /**
     * 私有化的构造函数，有类内部来管理该实例
     *
     * @param context         上下文对象
     * @param itemLayoutResId item的布局文件的资源ID
     * @param position        BaseAdapter.getView()的传入参数
     * @param parent          BaseAdapter.getView()的传入参数
     */
    private ZViewHolder(Context context, int itemLayoutResId, int position, ViewGroup parent) {
        this.views = new SparseArray<View>();
        this.position = position;
        this.convertView = LayoutInflater.from(context).inflate(itemLayoutResId, parent, false);
        convertView.setTag(this);
    }

    /**
     * 得到一个ViewHolder对象
     *
     * @param context         上下文对象
     * @param itemLayoutResId item的布局文件的资源ID
     * @param position        BaseAdapter.getView()的传入参数
     * @param convertView     BaseAdapter.getView()的传入参数
     * @param parent          BaseAdapter.getView()的传入参数
     * @return 一个ViewHolder对象
     */
    public static ZViewHolder getViewHolder(Context context, int itemLayoutResId, int position,
                                            View convertView, ViewGroup parent) {
        if (convertView == null) {
            return new ZViewHolder(context, itemLayoutResId, position, parent);
        } else {
            ZViewHolder viewHolder = (ZViewHolder) convertView.getTag();
            viewHolder.position = position; // 这里要更新一下position，因为position一直发生变化
            return viewHolder;
        }
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * 【核心部分】
     * 根据控件的资源ID，获取控件
     *
     * @param viewResId 控件的资源ID
     * @return 控件的引用
     */
    public <T extends View> T getView(int viewResId) {
        View view = views.get(viewResId);
        if (view == null) {
            view = convertView.findViewById(viewResId);
            views.put(viewResId, view);
        }
        return (T) view;
    }


    public ZViewHolder setText(int viewResId, String text) {
        TextView tv = getView(viewResId);
        tv.setText(text);
        return this;
    }

    public ZViewHolder setText(int viewResId, int textResId) {
        TextView tv = getView(viewResId);
        tv.setText(ZApplication.getInstance().getString(textResId));
        return this;
    }

    public ZViewHolder setDrawableIn(int viewResId, @DrawableRes int left, @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
        TextView tv = getView(viewResId);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public ZViewHolder setDrawableInLeft(int viewResId, @DrawableRes int left) {
        TextView tv = getView(viewResId);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, 0, 0, 0);
        return this;
    }

    public ZViewHolder setDrawableInTop(int viewResId, @DrawableRes int top) {
        TextView tv = getView(viewResId);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0);
        return this;
    }

    public ZViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public ZViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ZViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ZViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }
}
