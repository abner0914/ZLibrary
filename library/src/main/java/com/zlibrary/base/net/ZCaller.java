package com.zlibrary.base.net;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.zlibrary.base.application.ZApplication;
import com.zlibrary.base.application.ZConfig;
import com.zlibrary.base.cache.ZCache;
import com.zlibrary.base.entity.ZReqEncode;
import com.zlibrary.base.entity.ZReqFile;
import com.zlibrary.base.entity.ZReqMothed;
import com.zlibrary.base.entity.ZUploadEntity;
import com.zlibrary.base.exception.LException;
import com.zlibrary.base.net.ZDownload.LDownloadStoppingEntity;
import com.zlibrary.base.util.ZDate;
import com.zlibrary.base.util.ZFormat;
import com.zlibrary.base.util.ZL;
import com.zlibrary.base.util.ZMD5;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ZCaller {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String RUNTIME_EXCEPTION = "网络请求失败";
    private static final String SEND_ERROR = "ERROR=0";
    private static ZCache<String> cache = null;

    /**
     * 禁止实例化
     */
    private ZCaller() {
    }

    /**
     * 网络请求方法
     *
     * @param url      ：请求地址
     * @param jsonStr  ：请求参数
     * @param useCache ：是否启用缓存
     * @param mothed   ：请求方式{@link ZReqMothed}
     * @param encoding ：请求编码{@link ZReqEncode}
     */
    public static String doConn(String url, String jsonStr, boolean useCache,
                                ZReqMothed mothed, ZReqEncode encoding) throws Exception {

        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网络请求地址不能为空");
        }
        String data = null;
        String encode = ZReqEncode.UTF8.getEncode();
        if (encoding != null) {
            encode = encoding.getEncode();
        }
        if (mothed == null) {
            if (TextUtils.isEmpty(jsonStr)) {
                data = doGet(url, useCache, encode);
            } else {
                data = doPost(url, jsonStr, useCache, encode);
            }
        } else {
            if (mothed == ZReqMothed.POST) {
                data = doPost(url, jsonStr, useCache, encode);
            } else if (mothed == ZReqMothed.GET) {
                data = doGet(url, useCache, encode);
            } else {
                throw new IllegalArgumentException("请求方式参数错误");
            }
        }
        return data;
    }

    /**
     * 返回缓存
     *
     * @param url
     * @return
     */
    private static String doGetCache(String url) {
        String data = null;
        if (cache != null)
            data = cache.get(url);
        return data;
    }

    /**
     * 添加缓存
     *
     * @param url
     * @param value
     */
    private static void doSetCache(String url, String value) {
        if (cache == null)
            cache = new ZCache<String>();
        cache.put(url, value);
    }

    /**
     * post 方式请求http
     *
     * @param url      ：路径
     * @param jsonStr  ：参数
     * @param useCache ：是否使用缓存
     */
    public static String doPost(String url, String jsonStr,
                                boolean useCache, String encoding) throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网络请求地址不能为空");
        }
        String data = null;
        if (useCache) {
            data = doGetCache(url);
            if (!ZFormat.isEmpty(data)) {
                return data;
            }
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        data = response.body().string();

        if (useCache && !ZFormat.isEqual(SEND_ERROR, data)) {
            doSetCache(url, data);
        }
        return data;
    }


    /**
     * 向流中写入指定字节长度的字符串，不足时补0
     *
     * @param dous:要写入的流对象
     * @param s:要写入的字符串
     * @param len:写入长度,不足补0
     */
    public static void writeString(OutputStream dous, String s, int len) {
        try {
            if (s == null) {
                s = "";
            }
            byte[] data = s.getBytes("ISO8859_1");
            if (data.length > len) {
                throw new RuntimeException("向流中写入的字符串超长！要写" + len + " 字符串是:" + s);
            }
            int srcLen = data.length;
            dous.write(data);
            while (srcLen < len) {
                dous.write('\0');
                srcLen++;
            }
        } catch (IOException e) {
            throw new RuntimeException("向流中写入指定字节长度的字符串失败：" + e.getMessage());
        }
    }

    @SuppressLint("DefaultLocale")
    public static byte[] getHeadStream(ZUploadEntity entity) throws IOException {
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        DataOutputStream dous = new DataOutputStream(byteout);
        //长度
        dous.writeInt(entity.getTotalLen());
        //
        dous.writeInt(entity.getCommandId());
        //序列号
        dous.writeInt(entity.getSeq());
        writeString(dous, "jpg", 10);
        writeString(dous, entity.getToken(), 50);
        //AccessToken+CommandId+Time_Stamp
        String timeStamp = ZDate.formatDate2String(new Date(), "MMddHHmmss");
        String checkSource = String.format("%s%d%s", entity.getToken(), entity.getCommandId(), timeStamp);
        writeString(dous, ZMD5.getMD5(checkSource), 32);
        writeString(dous, timeStamp, 10);
        dous.writeByte(0);  //0未压缩   1压缩
        dous.writeByte(entity.getClientType());  //客户端类型   android:3  ios：4
        dous.writeInt(entity.getVersionCode());
        byte[] b = byteout.toByteArray();
        dous.close();
        return b;
    }

    /**
     * get方式请求http
     */
    public static String doGet(String url, boolean useCache, String encoding)
            throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网络请求地址不能为空");
        }
        String data = null;
        if (useCache) {
            data = doGetCache(url);
            if (!ZFormat.isEmpty(data)) {
                return data;
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        data = response.body().string();

        if (useCache && !ZFormat.isEqual(SEND_ERROR, data)) {
            doSetCache(url, data);
        }
        return data;
    }

    /**
     * 从网络上下载文件
     *
     * @param url 下载路径
     * @return
     */
    public static String doDownloadFile(String url, String savePath,
                                        String saveName) throws Exception {
        return doDownloadFile(url, savePath, saveName, null, null);
    }

    /**
     * 从网络上下载文件
     *
     * @param url      下载路径
     * @param progress 下载进度接口
     * @return
     */
    public static String doDownloadFile(String url, String savePath,
                                        String saveName, IZNetworkProgress progress,
                                        LDownloadStoppingEntity isStopping) throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网络请求地址不能为空");
        }
        File filePath = new File(savePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File res = new File(filePath, saveName);
        InputStream inputStream = null;
        FileOutputStream outStream = null;
        int count = 0;
        int current = 0;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setConnectTimeout(ZConfig.REQUEST_TIMEOUT);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                count = conn.getContentLength();
                refreshProgress(progress, count, 0);
                outStream = new FileOutputStream(res);
                inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1
                        && !isStopping.isStopping) {
                    outStream.write(buffer, 0, len);
                    current += len;
                    refreshProgress(progress, count, current);
                }
            }
        } catch (Exception e) {
            ZL.e(LException.getStackMsg(e));
        } finally {
            refreshProgress(progress, count, count);
            try {
                if (inputStream != null)
                    inputStream.close();
                inputStream = null;
            } catch (Exception e2) {
                ZL.e(LException.getStackMsg(e2));
            }
            try {
                if (outStream != null)
                    outStream.close();
                outStream = null;
            } catch (Exception e2) {
                ZL.e(LException.getStackMsg(e2));
            }
        }
        return res.getPath();
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url
     * @param params
     * @param files
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String doUploadFile(String url, Map<String, String> params,
                                      List<ZReqFile> files, ZReqEncode encoding) throws Exception {
        return doUploadFile(url, params, files, encoding, null);
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url
     * @param params
     * @param files
     * @return
     * @throws IOException
     */
    public static String doUploadFile(String url, Map<String, String> params,
                                      List<ZReqFile> files, ZReqEncode encoding,
                                      IZNetworkProgress progress) throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网络请求地址不能为空");
        }
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = encoding.getEncode();

        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", CHARSET);
        conn.setRequestProperty(ZApplication.getInstance().getSessionKey(),
                ZApplication.getInstance().getSessionValue());
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        InputStream in = null;
        // 发送文件数据
        String resStr = SEND_ERROR;
        if (files != null) {
            int filesLength = 0;
            try {
                for (ZReqFile file : files) {
                    filesLength += file.getLength();
                }
                int current = 0;
                refreshProgress(progress, filesLength, current);
                for (ZReqFile file : files) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                            + file.getName() + "\"" + LINEND);
                    sb1.append("Content-Type: " + file.getType().getType()
                            + "; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());

                    InputStream is = null;
                    try {
                        is = new FileInputStream(file.getFile());
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                            current += len;
                            refreshProgress(progress, filesLength, current);
                        }
                    } catch (Exception e) {
                        ZL.e(LException.getStackMsg(e));
                    } finally {
                        if (is != null) {
                            is.close();
                            is = null;
                        }
                    }

                    outStream.write(LINEND.getBytes());
                }

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
                        .getBytes();
                outStream.write(end_data);
                outStream.flush();
                // 得到响应码
                int res = conn.getResponseCode();
                if (res == 200) {
                    in = conn.getInputStream();
                    int ch;
                    StringBuilder sb2 = new StringBuilder();
                    while ((ch = in.read()) != -1) {
                        sb2.append((char) ch);
                    }
                    String data = sb2.toString();
                    if (TextUtils.isEmpty(data)) {
                        throw new ConnectException(RUNTIME_EXCEPTION);
                    }
                    resStr = ZFormat.JSONTokener(data);
                } else {
                    throw new ConnectException(RUNTIME_EXCEPTION);
                }
            } catch (Exception e) {
                ZL.e(LException.getStackMsg(e));
            } finally {
                refreshProgress(progress, filesLength, filesLength);
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (outStream != null) {
                    outStream.close();
                    outStream = null;
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } else {
            throw new NullPointerException("需要上传的文件集合不能为空");
        }
        return resStr;
    }

    /**
     * 刷新进度
     *
     * @param progress
     * @param count
     * @param current
     */
    private static void refreshProgress(IZNetworkProgress progress, int count,
                                        int current) {
        if (progress != null) {
            progress.sendProgress(count, current);
        }
    }

    /**
     * 通过InputStream获得UTF-8格式的String
     *
     * @param stream
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String convertStreamToString(final InputStream stream)
            throws Exception {
        return convertStreamToString(stream, ZReqEncode.UTF8.getEncode());
    }

    /**
     * 通过InputStream获得charsetName格式的String
     *
     * @param inSream
     * @param charsetName 指定格式
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String convertStreamToString(InputStream inSream,
                                               String charsetName) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inSream.close();
        return new String(data, charsetName);
    }

}
