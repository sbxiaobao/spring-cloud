package com.kafka.common.serialization;

import java.io.Closeable;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public interface Serializer<T> extends Closeable {

	void configure(Map<String, ?> configs, boolean isKey);

	byte[] serialize(String topic, T data);

	@Override
	void close();
}
