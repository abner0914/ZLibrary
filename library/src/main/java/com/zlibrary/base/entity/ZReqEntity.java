package com.zlibrary.base.entity;

import java.util.List;
import java.util.Map;


public class ZReqEntity implements Cloneable {

    /**
     * 需要上传的文件参数
     */
    ZUploadEntity uploadParams;
    /**
     * 网络请求地址
     */
    private String mUrl;
    /**
     * 网络请求匿名参数
     */
    private String mBody;
    /**
     * 网络请求参数
     */
    private Map<String, String> mParams;
    /**
     * 需要上传的文件集合
     */
    private List<ZReqFile> mFileParams;
    /**
     * 网络请求模式
     */
    private ZReqMothed mReqMode;

    /**
     * 网络请求编码方式
     */
    private ZReqEncode mReqEncode;

    /**
     * 网络请求是否启用缓存
     */
    private boolean mUseCache;

    public ZReqEntity() {
        init(null, null, null, null, ZReqMothed.GET, ZReqEncode.UTF8, false);
    }

    /**
     * 网络请求封装实体<br/>
     * 无参，以GET方式请求，UTF-8编码，不启用缓存
     *
     * @param url ：地址
     */
    public ZReqEntity(String url) {
        init(url, null, null, null, ZReqMothed.GET, ZReqEncode.UTF8, false);
    }

    /**
     * 网络请求封装实体<br/>
     * 以POST方式请求，UTF-8编码，不启用缓存
     *
     * @param url    ：地址
     * @param params ：参数
     */
    public ZReqEntity(String url, Map<String, String> params) {
        init(url, null, params, null, ZReqMothed.POST, ZReqEncode.UTF8, false);
    }

    /**
     * 网络请求封装实体<br/>
     * UTF-8编码，不启用缓存
     *
     * @param url  ：地址
     * @param body ：参数
     * @param mode ：请求模式
     */
    public ZReqEntity(String url, String body, ZReqMothed mode) {
        init(url, body, null, null, mode, ZReqEncode.UTF8, false);
    }

    /**
     * 网络请求封装实体<br/>
     * UTF-8编码，不启用缓存
     *
     * @param url    ：地址
     * @param params ：参数
     * @param mode   ：请求模式
     */
    public ZReqEntity(String url, Map<String, String> params, ZReqMothed mode) {
        init(url, null, params, null, mode, ZReqEncode.UTF8, false);
    }

    /**
     * 网络请求封装实体<br/>
     * 不启用缓存
     *
     * @param url      ：地址
     * @param params   ：参数
     * @param mode     ：请求模式
     * @param encoding ：请求编码
     */
    public ZReqEntity(String url, Map<String, String> params, ZReqMothed mode,
                      ZReqEncode encoding) {
        init(url, null, params, null, mode, encoding, false);
    }

    /**
     * 网络请求封装实体
     *
     * @param url      ：地址
     * @param params   ：参数
     * @param mode     ：请求模式
     * @param encoding ：请求编码
     * @param useCache ：启用缓存
     */
    public ZReqEntity(String url, Map<String, String> params, ZReqMothed mode,
                      ZReqEncode encoding, boolean useCache) {
        init(url, null, params, null, mode, encoding, useCache);
    }

    /**
     * 网络请求封装实体
     *
     * @param url      ：地址
     * @param params   ：参数
     * @param list     ：文件集合
     * @param encoding ：编码
     */
    public ZReqEntity(String url, Map<String, String> params,
                      List<ZReqFile> list, ZReqEncode encoding) {
        init(url, null, params, list, ZReqMothed.POST, encoding, false);
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public void setParams(Map<String, String> params) {
        this.mParams = params;
    }

    public ZReqMothed getReqMode() {
        return mReqMode;
    }

    public void setReqMode(ZReqMothed mReqMode) {
        this.mReqMode = mReqMode;
    }

    public ZReqEncode getReqEncode() {
        return mReqEncode;
    }

    public void setReqEncode(ZReqEncode mReqEncode) {
        this.mReqEncode = mReqEncode;
    }

    public boolean getUseCache() {
        return mUseCache;
    }

    public void setUseCache(boolean mUseCache) {
        this.mUseCache = mUseCache;
    }

    public List<ZReqFile> getFileParams() {
        return mFileParams;
    }

    public void setFileParamsList(List<ZReqFile> list) {
        this.mFileParams = list;
    }

    public ZUploadEntity getUploadParams() {
        return uploadParams;
    }

    public void setUploadParams(ZUploadEntity uploadParams) {
        this.uploadParams = uploadParams;
    }

    private void init(String url, String body, Map<String, String> params,
                      List<ZReqFile> list, ZReqMothed mode, ZReqEncode encoding,
                      boolean useCache) {
        this.mUrl = url;
        this.mBody = body;
        this.mParams = params;
        this.mFileParams = list;
        this.mReqMode = mode;
        this.mReqEncode = encoding;
        this.mUseCache = useCache;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("地址：").append(this.mUrl);
        sb.append("<--->");
        sb.append("参数：").append(this.mParams);
        sb.append("<--->");
        sb.append("文件：").append(this.mFileParams);
        sb.append("<--->");
        sb.append("模式：").append(this.mReqMode.getMothed());
        sb.append("<--->");
        sb.append("编码：").append(this.mReqEncode.getEncode());
        sb.append("<--->");
        sb.append("缓存：").append(this.mUseCache ? "不启用" : "启用");
        return sb.toString();
    }

}
