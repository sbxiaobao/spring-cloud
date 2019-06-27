package com.dianping.cat.message;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface Message {

    String SUCCESS = "0";

    void addData(String keyValuePairs);

    void addData(String key, Object value);

    void setStatus(String status);

    void setStatus(Throwable t);

    boolean isCompleted();

    void complete();

    long getTimestamp();

    String getType();

    String getName();

    String getStatus();

    Object getData();
}