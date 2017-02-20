package com.zlibrary.base.util;

/**
 * 位运算 工具类
 */
public class ZBits {

    /**
     * 设置指定位
     *
     * @param x
     * @param bit
     * @return
     */
    public static int bit_set(int x, int bit) {
        return ((x) |= bit_msk(bit));
    }

    /**
     * 测试指定位<br>
     * 测试数值x第bit位是否为1
     *
     * @param x
     * @param bit 指定索引位，从0开始,此处修改bug,原来翻译错误：(((x) & bit_msk (bit)) ==1 )
     * @return
     */
    public static boolean bit_tst(int x, int bit) {
        return (((x) & bit_msk(bit)) == bit_msk(bit));
    }

    /**
     * 清除指定位
     *
     * @param x
     * @param bit
     * @return
     */
    public static int bit_clr(int x, int bit) {
        return ((x) &= ~bit_msk(bit));
    }

    /**
     * 位索引从0开始
     *
     * @param bit
     * @return
     */
    public static int bit_msk(int bit) {
        return (1 << (bit));
    }
}