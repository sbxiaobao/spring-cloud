package com.kafka.clients.producer;

import com.kafka.common.Cluster;
import com.kafka.common.Node;
import com.kafka.common.PartitionInfo;
import com.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public final class Metadata {

	private static final Logger logger = LoggerFactory.getLogger(Metadata.class);

	private final long refreshBackoffMs;
	private final long metadataExpireMs;
	private int version;
	private long lastRefreshMs;
	private long lastSuccessfulRefreshMs;
	private Cluster cluster;
	private boolean needUpdate;
	private final Set<String> topics;
	private final List<Listener> listeners;
	private boolean needMetadataForAllTopics;

	public Metadata() {
		this(100L, 60 * 60 * 1000L);
	}

	public Metadata(long refreshBackoffMs, long metadataExpireMs) {
		this.refreshBackoffMs = refreshBackoffMs;
		this.metadataExpireMs = metadataExpireMs;
		this.lastRefreshMs = 0L;
		this.lastSuccessfulRefreshMs = 0L;
		this.version = 0;
		this.cluster = Cluster.empty();
		this.needUpdate = false;
		this.topics = new HashSet<>();
		this.listeners = new ArrayList<>();
		this.needMetadataForAllTopics = false;
	}

	public synchronized Cluster fetch() {
		return this.cluster;
	}

	public synchronized void add(String topic) {
		topics.add(topic);
	}

	public synchronized long timeToNextUpdate(long nowMs) {
		long timeToExpire = needUpdate ? 0 : Math.max(this.lastSuccessfulRefreshMs + this.metadataExpireMs - nowMs, 0);
		long timeToAllowUpdate = this.lastRefreshMs + this.refreshBackoffMs - nowMs;
		return Math.max(timeToExpire, timeToAllowUpdate);
	}

	public synchronized int requestUpdate() {
		this.needUpdate = true;
		return this.version;
	}

	/**
	 * Check whether an update has been explicitly requested.
	 *
	 * @return true if an update was requested, false otherwise
	 */
	public synchronized boolean updateRequested() {
		return this.needUpdate;
	}

	/**
	 * Wait for metadata update until the current version is larger than the last version we know of
	 */
	public synchronized void awaitUpdate(final int lastVersion, final long maxWaitMs) throws InterruptedException {
		if (maxWaitMs < 0) {
			throw new IllegalArgumentException("Max time to wait for metadata updates should not be < 0 milli seconds");
		}
		long begin = System.currentTimeMillis();
		long remainingWaitMs = maxWaitMs;
		while (this.version <= lastVersion) {
			if (remainingWaitMs != 0)
				wait(remainingWaitMs);
			long elapsed = System.currentTimeMillis() - begin;
			if (elapsed >= maxWaitMs)
				throw new TimeoutException("Failed to update metadata after " + maxWaitMs + " ms.");
			remainingWaitMs = maxWaitMs - elapsed;
		}
	}

	/**
	 * Replace the current set of topics maintained to the one provided
	 *
	 * @param topics
	 */
	public synchronized void setTopics(Collection<String> topics) {
		if (!this.topics.containsAll(topics))
			requestUpdate();
		this.topics.clear();
		this.topics.addAll(topics);
	}

	/**
	 * Get the list of topics we are currently maintaining metadata for
	 */
	public synchronized Set<String> topics() {
		return new HashSet<String>(this.topics);
	}

	/**
	 * Check if a topic is already in the topic set.
	 *
	 * @param topic topic to check
	 * @return true if the topic exists, false otherwise
	 */
	public synchronized boolean containsTopic(String topic) {
		return this.topics.contains(topic);
	}

	/**
	 * Update the cluster metadata
	 */
	public synchronized void update(Cluster cluster, long now) {
		this.needUpdate = false;
		this.lastRefreshMs = now;
		this.lastSuccessfulRefreshMs = now;
		this.version += 1;

		for (Listener listener : listeners)
			listener.onMetadataUpdate(cluster);

		// Do this after notifying listeners as subscribed topics' list can be changed by listeners
		this.cluster = this.needMetadataForAllTopics ? getClusterForCurrentTopics(cluster) : cluster;

		notifyAll();
		logger.debug("Updated cluster metadata version {} to {}", this.version, this.cluster);
	}

	public synchronized void failedUpdate(long now) {
		this.lastRefreshMs = now;
	}

	public synchronized int version() {
		return this.version;
	}

	public synchronized long lastSuccessfulUpdate() {
		return this.lastSuccessfulRefreshMs;
	}

	public long refreshBackoff() {
		return refreshBackoffMs;
	}

	public synchronized void needMetadataForAllTopics(boolean needMetadataForAllTopics) {
		this.needMetadataForAllTopics = needMetadataForAllTopics;
	}

	public synchronized boolean needMetadataForAllTopics() {
		return this.needMetadataForAllTopics;
	}

	public synchronized void addListener(Listener listener) {
		this.listeners.add(listener);
	}

	public synchronized void removeListener(Listener listener) {
		this.listeners.remove(listener);
	}

	public interface Listener {
		void onMetadataUpdate(Cluster cluster);
	}

	private Cluster getClusterForCurrentTopics(Cluster cluster) {
		Set<String> unauthorizedTopics = new HashSet<>();
		Collection<PartitionInfo> partitionInfos = new ArrayList<>();
		List<Node> nodes = Collections.emptyList();
		if (cluster != null) {
			unauthorizedTopics.addAll(cluster.unauthorizedTopics());
			unauthorizedTopics.retainAll(this.topics);

			for (String topic : this.topics) {
				partitionInfos.addAll(cluster.partitionsForTopic(topic));
			}
			nodes = cluster.nodes();
		}
		return new Cluster(nodes, partitionInfos, unauthorizedTopics);
	}
}