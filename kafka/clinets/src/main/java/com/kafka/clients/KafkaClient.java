package com.kafka.clients;

import com.kafka.common.Node;

import java.io.Closeable;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/9/29
 */
public interface KafkaClient extends Closeable {

	boolean isReady(Node node, long now);

	boolean ready(Node node, long now);

	long connectionDelay(Node node, long now);

	boolean connectionFailed(Node node);

//	void send(clientre)
}
