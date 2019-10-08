package com.kafka.clients.producer.internals;

import com.kafka.clients.producer.ProducerInterceptor;
import com.kafka.clients.producer.ProducerRecord;
import com.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/30
 */
public class ProducerInterceptors<K, V> implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(ProducerInterceptors.class);

	private final List<ProducerInterceptor<K, V>> interceptors;

	public ProducerInterceptors(List<ProducerInterceptor<K, V>> interceptors) {
		this.interceptors = interceptors;
	}

	public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
		for (ProducerInterceptor<K, V> interceptor : this.interceptors) {
			try {
				record = interceptor.onSend(record);
			} catch (Exception e) {
				if (record != null) {
					logger.warn("Error executing interceptor onSend callback for topic: {}, partition:{}", record.topic(), record.partition());
				} else {
					logger.warn("Error executing interceptor onSend callback", e);
				}
			}
		}
		return record;
	}

	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		for (ProducerInterceptor<K, V> interceptor : this.interceptors) {
			try {
				interceptor.onAcknowledgement(metadata, exception);
			} catch (Exception e) {
				logger.warn("Error executing interceptor onAcknowledgement callback", e);
			}
		}
	}

	@Override
	public void close() throws IOException {
		for (ProducerInterceptor<K, V> interceptor : this.interceptors) {
			try {
				interceptor.close();
			} catch (Exception e) {
				logger.error("Failed to close producer interceptor", e);
			}
		}
	}
}
