package com.kafka.clients.producer.internals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public final class RecordBatch {

	private static final Logger logger = LoggerFactory.getLogger(RecordBatch.class);

	public int recordCount = 0;
	public int maxRecordSize = 0;
	public volatile int attempts = 0;
	public final long createdMs;
	public long drainedMs;
//	public final memoryrec
}
