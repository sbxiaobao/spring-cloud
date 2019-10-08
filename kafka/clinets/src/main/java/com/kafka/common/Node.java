package com.kafka.common;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public class Node {

	private static final Node NO_NODE = new Node(-1, "", -1);

	private final int id;
	private final String idString;
	private final String host;
	private final int port;
	private final String rack;

	public Node(int id, String host, int port) {
		this(id, host, port, null);
	}

	public Node(int id, String host, int port, String rack) {
		super();
		this.id = id;
		this.idString = Integer.toString(id);
		this.host = host;
		this.port = port;
		this.rack = rack;
	}

	public static Node noNode() {
		return NO_NODE;
	}

	public boolean isEmpty() {
		return host == null || host.isEmpty() || port < 0;
	}

	public int id() {
		return id;
	}

	public String idString() {
		return idString;
	}

	public String host() {
		return host;
	}
}
