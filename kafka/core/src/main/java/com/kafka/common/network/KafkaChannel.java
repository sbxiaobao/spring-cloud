package com.kafka.common.network;

import java.net.InetAddress;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class KafkaChannel {
    private final String id;
    private final TransportLayer transportLayer;

    public KafkaChannel(String id, TransportLayer transportLayer) {
        this.id = id;
        this.transportLayer = transportLayer;
    }

    public String getId() {
        return id;
    }

    public InetAddress socketAddress() {
        return transportLayer.socketChannel().socket().getInetAddress();
    }
}
