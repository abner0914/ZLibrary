package com.zlibrary.base.viewPagerIndicator.indicator.slidebar;

import android.view.View;

/**
 * Indicator的滑动块
 */
public interface ScrollBar {
    /**
     * SlideView显示的高度
     *
     * @return
     */
    int getHeight(int tabHeight);

    /**
     * SlideView 显示的宽度
     *
     * @param tabWidth
     * @return
     */
    int getWidth(int tabWidth);

    /**
     * 滑动显示的view
     *
     * @return
     */
    View getSlideView();

    /**
     * 位置
     *
     * @return
     */
    Gravity getGravity();

    /**
     * 当page滑动的时候调用
     */
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    enum Gravity {
        /**
         * 顶部占位
         */
        TOP,
        /**
         * 顶部覆盖在Indicator上
         */
        TOP_FLOAT,
        /**
         * 底部占位
         */
        BOTTOM,
        /**
         * 底部覆盖在Indicator上
         */
        BOTTOM_FLOAT,
        /**
         * 中间覆盖在Indicator上
         */
        CENTENT,
        /**
         * 中间，被Indicator覆盖
         */
        CENTENT_BACKGROUND
    }
}
