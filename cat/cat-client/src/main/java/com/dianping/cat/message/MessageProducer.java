package com.dianping.cat.message;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface MessageProducer {

    Transaction newTransaction(String type, String name);

    Heartbeat newHeartbeat(String type, String name);

    void logEvent(String type, String name);

    void logEvent(String type, String name, String status, String nameValuePairs);

    void logError(String message, Throwable cause);

    void logError(Throwable cause);
}