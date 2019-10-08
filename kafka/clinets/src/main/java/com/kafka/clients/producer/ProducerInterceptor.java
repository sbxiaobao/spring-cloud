package com.kafka.clients.producer;

import com.kafka.common.Configurable;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/30
 */
public interface ProducerInterceptor<K, V> extends Configurable {

	ProducerRecord<K, V> onSend(ProducerRecord<K, V> record);

	void onAcknowledgement(RecordMetadata metadata, Exception exception);

	void close();
}