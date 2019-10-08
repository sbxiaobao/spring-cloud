package com.kafka.clients.producer;

import com.kafka.clients.producer.internals.RecordAccumulator;
import com.kafka.clients.producer.internals.Sender;
import com.kafka.common.PartitionInfo;
import com.kafka.common.TopicPartition;
import com.kafka.common.errors.TimeoutException;
import com.kafka.common.errors.TopicAuthorizationException;
import com.kafka.common.record.CompressionType;
import com.kafka.common.serialization.Serializer;
import com.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class KafkaProducer<K, V> implements Producer<K, V> {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	private static final AtomicInteger PRODUCER_CLIENT_ID_SEQUENCE = new AtomicInteger(1);
	private static final String JMX_PREFIX = "kafka.producer";

	private String clientId;
	private final Partitioner partitioner;
	private final int maxRequestSize;
	private final long totalMemorySize;
	private final Metadata metadata;
	private final RecordAccumulator accumulator;
	private final Sender sender;
	private final Thread ioThread;
	private final CompressionType compressionType;
	private final Time time;
	private final Serializer<K> keySerializer;
	private final Serializer<V> valueSerializer;
//	private final pro

	@Override
	public Future<RecordMetadata> send(ProducerRecord<K, V> record) {
		return null;
	}

	@Override
	public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
		return null;
	}

	private Future<RecordMetadata> doSend(ProducerRecord<K, V> record, Callback callback) {
		TopicPartition tp = null;
		return null;
	}

	private long waitOneMetadata(String topic, long maxWaitMs) throws InterruptedException {
		if (!this.metadata.containsTopic(topic)) {
			this.metadata.add(topic);
		}

		if (metadata.fetch().partitionsForTopic(topic) != null) {
			return 0;
		}

		long begin = time.milliseconds();
		long remainingWaitMs = maxWaitMs;
		while (metadata.fetch().partitionsForTopic(topic) == null) {
			logger.trace("Requesting metadata update for topic {}.", topic);
			int version = metadata.requestUpdate();
			sender.wakeup();
			metadata.awaitUpdate(version, remainingWaitMs);
			long elapsed = time.milliseconds() - begin;
			if (elapsed >= maxWaitMs) {
				throw new TimeoutException("Failed to update metadata after " + maxWaitMs + " ms.");
			}
			if (metadata.fetch().unauthorizedTopics().contains(topic)) {
				throw new TopicAuthorizationException(topic);
			}
			remainingWaitMs = maxWaitMs - elapsed;
		}
		return time.milliseconds() - begin;
	}

	@Override
	public void flush() {

	}

	@Override
	public List<PartitionInfo> partitionsFor(String topic) {
		return null;
	}

	@Override
	public void close() {

	}

	@Override
	public void close(long timeout, TimeUnit unit) {

	}
}
