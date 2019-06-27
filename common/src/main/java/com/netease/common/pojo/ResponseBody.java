package com.netease.common.pojo;

import com.netease.common.codes.ApiCodes;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class ResponseBody {
    private int code = ApiCodes.SUCCESS.getCode();
    private Object result = ApiCodes.SUCCESS.getResult();
    private String message = ApiCodes.SUCCESS.getMessage();

    public ResponseBody(){
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}