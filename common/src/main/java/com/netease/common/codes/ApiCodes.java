package com.netease.common.codes;

import org.springframework.http.HttpStatus;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public enum ApiCodes implements Codes {
    SUCCESS(HttpStatus.OK.value(), "Success", "成功");

    private int code;
    private String message;
    private String result;

    ApiCodes(int code, String message, String result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getResult() {
        return result;
    }
}
