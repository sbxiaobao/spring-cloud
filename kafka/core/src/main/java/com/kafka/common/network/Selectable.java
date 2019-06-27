package com.kafka.common.network;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public interface Selectable {

    int USE_DEFAULT_BUFFER_SIZE = -1;

    void connect(String id, InetSocketAddress address, int sendBufferSize, int receiverBufferSize) throws IOException;

    void wakeup();

    void close();

    void close(String id);

    KafkaChannel channel(String id);
}