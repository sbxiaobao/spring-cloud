package com.kafka.common;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class KafkaException extends RuntimeException {

    public KafkaException(String message) {
        this(message, null);
    }

    public KafkaException(Throwable t) {
        this("", t);
    }

    public KafkaException(String message, Throwable t) {
        super(message, t);
    }
}
