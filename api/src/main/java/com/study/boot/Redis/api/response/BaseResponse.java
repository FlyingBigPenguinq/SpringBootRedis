package com.study.boot.Redis.api.response;

/**
 * 项目的统一响应模型类
 * @Date: 2019/12/20 17:23
 **/
public class BaseResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}























