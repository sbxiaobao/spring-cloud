package com.kafka.clients.producer.internals;

import com.kafka.clients.KafkaClient;
import com.kafka.clients.producer.Metadata;
import com.kafka.common.Cluster;
import com.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class Sender implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(Sender.class);

	private final KafkaClient client;
	private final RecordAccumulator accumulator;
	private final Metadata metadata;
	private final boolean guaranteeMessageOrder;
	private final int maxRequestSize;
	private final short acks;
	private final int retries;
	private final Time time;
	private volatile boolean running;
	private String clientId;
	private final int requestTimeout;

	public Sender(KafkaClient client, Metadata metadata, RecordAccumulator accumulator, boolean guaranteeMessageOrder,
				  int maxRequestSize, short acks, int retries, Time time, String clientId, int requestTimeout) {
		this.client = client;
		this.accumulator = accumulator;
		this.metadata = metadata;
		this.guaranteeMessageOrder = guaranteeMessageOrder;
		this.maxRequestSize = maxRequestSize;
		this.running = true;
		this.acks = acks;
		this.retries = retries;
		this.time = time;
		this.clientId = clientId;
		this.requestTimeout = requestTimeout;
	}

	@Override
	public void run() {
		logger.debug("Starting kafka producer I/O thread.");

		while (running) {
		}
	}

	void run(long now) {
		Cluster cluster = metadata.fetch();
//		RecordAccumulator.re
	}

	public void wakeup() {

	}
}