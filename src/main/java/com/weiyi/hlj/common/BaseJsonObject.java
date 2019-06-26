package com.weiyi.hlj.common;

/**
 * Created by qinxy on 2019/4/2.
 * 统一返回结构
 */
public class BaseJsonObject<T> {

    /**
     * 成功状态code
     */
    private static final int SUCCESS_CODE = 0;

    /**
     * 失败状态code
     */
    private static final int FAIL_CODE = 1;
    /**
     * 成功：1 失败：0 其他状态code自定义
     */
    private int code;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 数据
     */
    private T data;
    /**
     * 消息
     */
    private String message;

    /**
     * 日期
     */
    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static int getFailCode() {
        return FAIL_CODE;
    }


    public static <E> BaseJsonObject<E> successResp() {
        return successResp("成功", null);
    }

    public static <E> BaseJsonObject<E> successResp(E e) {
        return successResp("成功", e);
    }

    public static <E> BaseJsonObject<E> successResp(String message, E e) {
        return response(SUCCESS_CODE, true, message, e);
    }

    public static <E> BaseJsonObject<E> failResp() {
        return failResp("失败", null);
    }

    public static <E> BaseJsonObject<E> failResp(String message) {
        return failResp(message, null);
    }

    public static <E> BaseJsonObject<E> failResp(String message, E e) {
        return response(FAIL_CODE, false, message, e);
    }

    public static <E> BaseJsonObject<E> response(int code, boolean success, String message, E e) {
        BaseJsonObject<E> json = new BaseJsonObject<E>();
        json.setCode(code);
        json.setSuccess(success);
        json.setDate(Commons.formatNow());
        json.setMessage(message);
        json.setData(e);
        return json;
    }
}
