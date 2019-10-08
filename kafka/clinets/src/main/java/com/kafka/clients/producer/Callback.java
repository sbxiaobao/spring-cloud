package com.kafka.clients.producer;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public interface Callback {

	void onCompletion(RecordMetadata metadata, Exception exception);
}
