package com.kafka.clients.producer;

import com.kafka.common.PartitionInfo;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public interface Producer<K, V> extends Closeable {

	Future<RecordMetadata> send(ProducerRecord<K, V> record);

	Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback);

	void flush();

	List<PartitionInfo> partitionsFor(String topic);

	void close();

	void close(long timeout, TimeUnit unit);
}