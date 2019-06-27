package com.kafka.common.network;

import com.kafka.common.KafkaException;

import java.nio.channels.SelectionKey;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public interface ChannelBuilder {

    void configure(Map<String, ?> configs) throws KafkaException;

    KafkaChannel buildChannel(String id, SelectionKey key, int maxReceiveSize) throws KafkaException;

    void close();
}
