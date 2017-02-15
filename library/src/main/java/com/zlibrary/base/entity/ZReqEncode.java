package com.zlibrary.base.entity;


public enum ZReqEncode {

    /**
     * 使用UTF-8编码请求网络连接
     */
    UTF8("UTF-8"),

    /**
     * 使用GBK编码请求网络连接
     */
    GBK("GBK");

    private String mEncoding;

    ZReqEncode(String encoding) {
        this.mEncoding = encoding;
    }

    public String getEncode() {
        return this.mEncoding;
    }
}
