package com.kafka.clients.producer.internals;

import com.kafka.clients.producer.Callback;
import com.kafka.common.Cluster;
import com.kafka.common.Node;
import com.kafka.common.TopicPartition;
import com.kafka.common.record.CompressionType;
import com.kafka.common.utils.CopyOnWriteMap;
import com.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public final class RecordAccumulator {

	private static final Logger logger = LoggerFactory.getLogger(RecordAccumulator.class);

	private volatile boolean closed;
	private final AtomicInteger flushedInProgress;
	private final AtomicInteger appendsInProgress;
	private final int batchSize;
	private final CompressionType compression;
	private final long lingerMs;
	private final long retryBackoffMs;
	private final BufferPool free;
	private final Time time;
	private final ConcurrentMap<TopicPartition, Deque<RecordBatch>> batches;
	private final IncompleteRecordBatches incomplete;
	private final Set<TopicPartition> muted;
	private int drainIndex;

	public RecordAccumulator(int batchSize, long totalSize, CompressionType compression, long lingerMs, long retryBackoffMs, Time time) {
		this.drainIndex = 0;
		this.closed = false;
		this.flushedInProgress = new AtomicInteger(0);
		this.appendsInProgress = new AtomicInteger(0);
		this.batchSize = batchSize;
		this.compression = compression;
		this.lingerMs = lingerMs;
		this.retryBackoffMs = retryBackoffMs;
		this.batches = new CopyOnWriteMap<>();
		this.free = new BufferPool(totalSize, batchSize, time);
		this.incomplete = new IncompleteRecordBatches();
		this.muted = new HashSet<>();
		this.time = time;
	}

	public RecordAppendResult append(TopicPartition tp, long timestamp, byte[] key, byte[] value, Callback callback, long maxTimeToBlock) {
		appendsInProgress.incrementAndGet();
		try {
//			Deque<RecordBatch> dq =
		} finally {
			appendsInProgress.decrementAndGet();
		}
		return null;
	}

	public ReadyCheckResult ready(Cluster cluster, long nowMs) {
		Set<Node> readyNodes = new HashSet<>();
		long nextReadyCheckDelayMs = Long.MAX_VALUE;
		boolean unknownLeadersExist = false;

		boolean exhausted = this.free.queued() > 0;
		for (Map.Entry<TopicPartition, Deque<RecordBatch>> entry : this.batches.entrySet()) {
			TopicPartition part = entry.getKey();
			Deque<RecordBatch> deque = entry.getValue();

			Node leader = cluster.leaderFor(part);
			if (leader == null) {
				unknownLeadersExist = true;
			} else if (!readyNodes.contains(leader) && !muted.contains(part)) {
				synchronized (deque) {
					RecordBatch batch = deque.peekFirst();
					if (batch != null) {
//						boolean backingOff = batch.attempts > 0 && batch.
					}
				}
			}
		}
	}

	public final static class RecordAppendResult {
		public final FutureRecordMetadata future;
		public final boolean batchIsFull;
		public final boolean newBatchCreated;

		public RecordAppendResult(FutureRecordMetadata future, boolean batchIsFull, boolean newBatchCreated) {
			this.future = future;
			this.batchIsFull = batchIsFull;
			this.newBatchCreated = newBatchCreated;
		}
	}

	public final static class ReadyCheckResult {
		public final Set<Node> readyNodes;
		public final long nextReadyCheckDelayMs;
		public final boolean unknownLeadersExist;

		public ReadyCheckResult(Set<Node> readyNodes, long nextReadyCheckDelayMs, boolean unknownLeadersExist) {
			this.readyNodes = readyNodes;
			this.nextReadyCheckDelayMs = nextReadyCheckDelayMs;
			this.unknownLeadersExist = unknownLeadersExist;
		}
	}

	private final static class IncompleteRecordBatches {
		private final Set<RecordBatch> incomplete;

		public IncompleteRecordBatches() {
			this.incomplete = new HashSet<>();
		}

		public void add(RecordBatch batch) {
			synchronized (incomplete) {
				this.incomplete.add(batch);
			}
		}

		public void remove(RecordBatch batch) {
			synchronized (incomplete) {
				boolean removed = this.incomplete.remove(batch);
				if (!removed) {
					throw new IllegalStateException("Remove from the incomplete set failed. This should be impossible.");
				}
			}
		}

		public Iterable<RecordBatch> all() {
			synchronized (incomplete) {
				return new ArrayList<>(this.incomplete);
			}
		}
	}
}