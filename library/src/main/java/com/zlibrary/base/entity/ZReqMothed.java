package com.zlibrary.base.entity;

public enum ZReqMothed {

    /**
     * 使用POST方式请求网络连接
     */
    POST("POST"),

    /**
     * 使用GET方式请求网络连接
     */
    GET("GET"),

    /**
     * 下载文件
     */
    DOWNLOAD("DOWNLOAD"),

    /**
     * 上传文件
     */
    UPLOAD("UPLOAD");

    private String mMothed;

    ZReqMothed(String mothed) {
        this.mMothed = mothed;
    }

    /**
     * 获取请求网络连接方式具体值
     *
     * @return
     */
    public String getMothed() {
        return this.mMothed;
    }

}
