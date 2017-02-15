package com.zlibrary.base.entity;

/**
 * 此类为网络请求提交文件类型的类<br/>
 * 暂时只收录了jpeg格式<br/>
 */
public enum ZReqFileType {

    JPEG("image/pjpeg");

    private String type;

    ZReqFileType(String str) {
        type = str;
    }

    public String getType() {
        return type;
    }

}
