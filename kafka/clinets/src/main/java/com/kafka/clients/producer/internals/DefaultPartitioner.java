package com.kafka.clients.producer.internals;

import com.kafka.clients.producer.Partitioner;
import com.kafka.common.Cluster;
import com.kafka.common.PartitionInfo;
import com.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class DefaultPartitioner implements Partitioner {

	private final AtomicInteger counter = new AtomicInteger(new Random().nextInt());

	private static int toPositive(int number) {
		return number & 0x7fffffff;
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		if (keyBytes == null) {
			int nextValue = counter.getAndIncrement();
			List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
			if (availablePartitions.size() > 0) {
				int part = DefaultPartitioner.toPositive(nextValue) % availablePartitions.size();
				return availablePartitions.get(part).partition();
			} else {
				return DefaultPartitioner.toPositive(nextValue) % numPartitions;
			}
		} else {
			return DefaultPartitioner.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
		}
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {
	}
}