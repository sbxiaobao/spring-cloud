package com.kafka.common;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/5
 */
public class BrokerNotAvailableException extends RuntimeException {

    public BrokerNotAvailableException(String message) {
        super(message);
    }

    public BrokerNotAvailableException() {
        this(null);
    }
}
