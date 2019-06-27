package com.kafka.common.network;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/30
 */
public interface TransportLayer extends ScatteringByteChannel, GatheringByteChannel {

    boolean ready();

    boolean finishConnect() throws IOException;

    void disconnect();

    boolean isConnected();

    SocketChannel socketChannel();

    void handshake() throws IOException;
}