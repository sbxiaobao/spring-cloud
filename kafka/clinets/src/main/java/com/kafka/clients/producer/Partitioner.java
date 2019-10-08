package com.kafka.clients.producer;

import com.kafka.common.Cluster;
import com.kafka.common.Configurable;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public interface Partitioner extends Configurable {

	int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster);

	void close();
}
