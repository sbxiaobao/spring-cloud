package com.kafka.common.serialization;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/30
 */
public class IntegerSerializer implements Serializer<Integer> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// nothing to do
	}

	@Override
	public byte[] serialize(String topic, Integer data) {
		if (data == null) {
			return null;
		}

		return new byte[]{
				(byte) (data >>> 24),
				(byte) (data >>> 16),
				(byte) (data >>> 8),
				data.byteValue()
		};
	}

	@Override
	public void close() {
		// nothing to do
	}
}