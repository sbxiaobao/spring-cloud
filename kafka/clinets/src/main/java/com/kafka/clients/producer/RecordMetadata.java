package com.kafka.clients.producer;

import com.kafka.common.TopicPartition;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class RecordMetadata {

	public static final int UNKNOWN_PARTITION = -1;

	private final long offset;
	private final long timestamp;
	private final long checksum;
	private final int serializedKeySize;
	private final int serializedValueSize;
	private final TopicPartition topicPartition;

	private RecordMetadata(TopicPartition topicPartition, long offset, long timestamp,
						   long checksum, int serializedKeySize, int serializedValueSize) {
		super();
		this.offset = offset;
		this.timestamp = timestamp;
		this.checksum = checksum;
		this.serializedKeySize = serializedKeySize;
		this.serializedValueSize = serializedValueSize;
		this.topicPartition = topicPartition;
	}

	public RecordMetadata(TopicPartition topicPartition, long baseOffset, long relativeOffset,
						  long timestamp, long checksum, int serializedKeySize, int serializedValueSize) {
		this(topicPartition, baseOffset == -1 ? baseOffset : baseOffset + relativeOffset,
				timestamp, checksum, serializedKeySize, serializedValueSize);
	}

	public long offset() {
		return this.offset;
	}

	public long timestamp() {
		return timestamp;
	}

	public long checksum() {
		return this.checksum;
	}

	public int serializedKeySize() {
		return this.serializedKeySize;
	}

	public int serializedValueSize() {
		return this.serializedValueSize;
	}

	public String topic() {
		return this.topicPartition.topic();
	}

	public int partition() {
		return this.topicPartition.partition();
	}

	@Override
	public String toString() {
		return topicPartition.toString() + "@" + offset;
	}
}