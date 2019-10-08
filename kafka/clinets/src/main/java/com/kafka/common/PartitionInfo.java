package com.kafka.common;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class PartitionInfo {

	private final String topic;
	private final int partition;
	private final Node leader;
	private final Node[] replicas;
	private final Node[] inSyncReplicas;

	public PartitionInfo(String topic, int partition, Node leader, Node[] replicas, Node[] inSyncReplicas) {
		this.topic = topic;
		this.partition = partition;
		this.leader = leader;
		this.replicas = replicas;
		this.inSyncReplicas = inSyncReplicas;
	}

	public String topic() {
		return topic;
	}

	public int partition() {
		return partition;
	}

	/**
	 * The node id of the node currently acting as a leader for this partition or null if there is no leader
	 */
	public Node leader() {
		return leader;
	}

	/**
	 * The complete set of replicas for this partition regardless of whether they are alive or up-to-date
	 */
	public Node[] replicas() {
		return replicas;
	}

	/**
	 * The subset of the replicas that are in sync, that is caught-up to the leader and ready to take over as leader if
	 * the leader should fail
	 */
	public Node[] inSyncReplicas() {
		return inSyncReplicas;
	}

	@Override
	public String toString() {
		return String.format("Partition(topic = %s, partition = %d, leader = %s, replicas = %s, isr = %s",
				topic,
				partition,
				leader == null ? "none" : leader.id(),
				fmtNodeIds(replicas),
				fmtNodeIds(inSyncReplicas));
	}

	/* Extract the node ids from each item in the array and format for display */
	private String fmtNodeIds(Node[] nodes) {
		StringBuilder b = new StringBuilder("[");
		for (int i = 0; i < nodes.length - 1; i++) {
			b.append(Integer.toString(nodes[i].id()));
			b.append(',');
		}
		if (nodes.length > 0) {
			b.append(Integer.toString(nodes[nodes.length - 1].id()));
			b.append(',');
		}
		b.append("]");
		return b.toString();
	}
}
