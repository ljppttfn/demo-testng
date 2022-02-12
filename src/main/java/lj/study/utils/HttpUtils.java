package lj.study.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);


    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 基于okHttp3 的 get 请求封装
     *
     * @param url     地址
     * @param headers headers,类型为 fastjson.JSONObject
     * @return string
     */
    public static String get(String url, JSONObject headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                headersBuilder.add(key, headers.getString(key));
            }
        }
        Headers okHeaders = headersBuilder.build();

        Request request = new Request.Builder().url(url).headers(okHeaders).build();

        String strRsp = null;
        try {
//            logger.debug("HttpUtils debug >>> get url : " + url);
            Response response = client.newCall(request).execute();
            strRsp = response.body().string();
            if (response.code() != 200) {
                logger.info(String.format("HttpUtils error, response code != 200 . url: %s,  rsp: %s ", url, strRsp));
            }
            return strRsp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * put
     *
     * @param url      url
     * @param headers  headers 可为空
     * @param body     body
     * @param file     可为空，当有文件上传时，file、fileType、fileName 都必须有值
     * @param fileType 同file
     * @param fileName 同file
     * @return rsp String
     */
    public static String post(String url, JSONObject body, JSONObject headers,
                              byte[] file, String fileType, String fileName) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                if (headers.getString(key) == null) {
                    logger.info("value为空，请排查， key= " + key);
                }
                headersBuilder.add(key, headers.getString(key));
            }
        }
        Headers okHeaders = headersBuilder.build();

        RequestBody okBody = null;

        if (headers != null && headers.getString("Content-Type").contains("application/json")) {
            // application/json 数据是个json
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okBody = RequestBody.create(body.toJSONString(), mediaType);
        } else if (headers != null && headers.getString("Content-Type").contains("application/x-www-form-urlencoded")) {
            // application/x-www-form-urlencoded 数据是个普通表单
            FormBody.Builder body1Builder = new FormBody.Builder();
            for (String key : body.keySet()) {
                body1Builder.add(key, body.getString(key));
            }
            okBody = body1Builder.build();
        } else {
            //multipart/form-data 数据里有文件
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            if (file != null && file.length > 0 && StringUtils.isNotEmpty(fileType)) {
                RequestBody fileBody = RequestBody.create(file, MediaType.parse(fileType));
                builder.addFormDataPart("file", fileName, fileBody);
            }
            if (body != null) {
                for (String key : body.keySet()) {
                    builder.addFormDataPart(key, body.getString(key));
                }
            }
            okBody = builder.build();
        }

        Request request = new Request.Builder().url(url).post(okBody).headers(okHeaders).build();
        try {
//            logger.debug("HttpUtils debug >>> post url : " + url);
//            logger.debug("HttpUtils debug >>> body : " + body.toJSONString());
            Response response = client.newCall(request).execute();
            String rspStringData = response.body().string();
            if (response.code() != 200) {
                logger.info(String.format("HttpUtils error, response code != 200 . url: %s,  rsp: %s ", url, rspStringData));
            }
            return rspStringData;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("post 异常：");
            logger.info(e.getMessage());
            return null;
        }
    }

    public static String post(String url, JSONObject body, @Nullable JSONObject headrs) {
        return postJSON(url, body, headrs);
    }

    public static String postJSON(String url, JSONObject body, @Nullable JSONObject headers) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put("Content-Type", "application/json; charset=utf-8");
        return post(url, body, headers, null, null, null);
    }

    public static String postFORM(String url, JSONObject body, @Nullable JSONObject headers) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return post(url, body, headers, null, null, null);
    }

    public static String postWithFile(String url, JSONObject body, JSONObject headers,
                                      byte[] file, String fileType, String fileName) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put("Content-Type", "multipart/form-data; charset=utf-8");
        return post(url, body, headers, file, fileType, fileName);
    }


    /**
     * put
     *
     * @param url      url
     * @param headers  headers 可为空
     * @param body     body
     * @param file     可为空，当有文件上传时，file、fileType、fileName 都必须有值
     * @param fileType 同file
     * @param fileName 同file
     * @return rsp String
     */
    public static String put(String url, JSONObject headers, JSONObject body,
                             byte[] file, String fileType, String fileName) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                headersBuilder.add(key, headers.getString(key));
            }
        }
        Headers okHeaders = headersBuilder.build();

        if (body == null) {
            body = new JSONObject();
        }

        RequestBody okBody = null;

        if (headers != null && headers.getString("Content-Type").contains("application/json")) {
            // application/json 数据是个json
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okBody = RequestBody.create(body.toJSONString(), mediaType);
        } else if (headers != null && headers.getString("Content-Type").contains("application/x-www-form-urlencoded")) {
            // application/x-www-form-urlencoded 数据是个普通表单
            FormBody.Builder body1Builder = new FormBody.Builder();
            for (String key : body.keySet()) {
                body1Builder.add(key, body.getString(key));
            }
            okBody = body1Builder.build();
        } else {
            //multipart/form-data 数据里有文件
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            if (file != null && file.length > 0 && StringUtils.isNotEmpty(fileType)) {
                RequestBody fileBody = RequestBody.create(file, MediaType.parse(fileType));
                builder.addFormDataPart("file", fileName, fileBody);
            }

            for (String key : body.keySet()) {
                builder.addFormDataPart(key, body.getString(key));
            }
            okBody = builder.build();
        }

        Request request = new Request.Builder().url(url).put(okBody).headers(okHeaders).build();
        try {
            logger.debug("HttpUtils debug >>> put url : " + url);
            logger.debug("HttpUtils debug >>> body : " + body.toJSONString());
            Response response = client.newCall(request).execute();
            String rspStringData = response.body().string();
            logger.debug("HttpUtils debug >>> response : " + rspStringData);
            return rspStringData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String put(String url, JSONObject body) {
        return putJSON(url, null, body);
    }

    public static String put(String url, JSONObject headers, JSONObject body) {
        return putJSON(url, headers, body);
    }

    public static String putJSON(String url, JSONObject headers, JSONObject body) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put("Content-Type", "application/json; charset=utf-8");
        return put(url, headers, body, null, null, null);
    }

    public static String putFORM(String url, JSONObject headers, JSONObject body) {
        if (headers == null) {
            headers = new JSONObject();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return put(url, headers, body, null, null, null);
    }


    /**
     * delete
     *
     * @param url     url
     * @param headers headers
     * @return rsp
     */
    public static String delete(String url, JSONObject headers) {
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                headersBuilder.add(key, headers.getString(key));
            }
        }
        Headers okHeaders = headersBuilder.build();

        Request request = new Request.Builder().url(url).headers(okHeaders).delete().build();
        try {
            logger.debug("HttpUtils debug >>> delete url : " + url);
            Response response = client.newCall(request).execute();
            String rspStringData = response.body().string();
            logger.debug("HttpUtils debug >>> response : " + rspStringData);
            return rspStringData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
