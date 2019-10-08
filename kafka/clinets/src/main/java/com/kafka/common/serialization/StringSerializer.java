package com.kafka.common.serialization;

import com.kafka.common.errors.SerializationException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/30
 */
public class StringSerializer implements Serializer<String> {

	private String encoding = "UTF8";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
		Object encodingValue = configs.get(propertyName);
		if (encodingValue == null) {
			encodingValue = configs.get("serializer.encoding");
		}
		if (encodingValue != null && encodingValue instanceof String) {
			encoding = (String) encodingValue;
		}
	}

	@Override
	public byte[] serialize(String topic, String data) {
		try {
			if (data == null) {
				return null;
			} else {
				return data.getBytes(encoding);
			}
		} catch (UnsupportedEncodingException e) {
			throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding);
		}
	}

	@Override
	public void close() {
		//nothing to do
	}
}
