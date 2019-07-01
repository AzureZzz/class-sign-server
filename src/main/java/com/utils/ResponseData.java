package com.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {


    private final String message;
    private final int code;
    private final Map<String, Object> data = new HashMap<String, Object>();


    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 请求成功
     *
     * @return
     */
    public static ResponseData ok() {
        return new ResponseData(200, "Ok");
    }

    /**
     * 未找到相关资源
     *
     * @return
     */
    public static ResponseData notFound() {
        return new ResponseData(404, "Not Found");
    }

    /**
     * 错误的请求
     *
     * @return
     */
    public static ResponseData badRequest() {
        return new ResponseData(400, "Bad Request");
    }

    /**
     * 不允许访问
     *
     * @return
     */
    public static ResponseData forbidden() {
        return new ResponseData(403, "Forbidden");
    }

    /**
     * 未授权
     *
     * @return
     */
    public static ResponseData unauthorized() {
        return new ResponseData(401, "unauthorized");
    }

    /**
     * 服务器错误
     *
     * @return
     */
    public static ResponseData serverInternalError() {
        return new ResponseData(500, "Server Internal Error");
    }

    /**
     * 客户端错误
     *
     * @return
     */
    public static ResponseData customerError() {
        return new ResponseData(1001, "customer Error");
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ResponseData putDataValue(String key, Object value) {
        data.put(key, value);
        return this;
    }
}
