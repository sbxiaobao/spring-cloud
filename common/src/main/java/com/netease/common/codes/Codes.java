package com.netease.common.codes;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface Codes {

    interface Start {
        int GLOBAL = 1000;
    }

    int getCode();

    String getMessage();

    String getResult();
}