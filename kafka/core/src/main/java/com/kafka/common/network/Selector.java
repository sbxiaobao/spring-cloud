package com.kafka.common.network;

import com.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class Selector implements Selectable {

    private static final Logger logger = LoggerFactory.getLogger(Selector.class);

    private final java.nio.channels.Selector nioSelector;
    private final Map<String, KafkaChannel> channels;
    private final Set<SelectionKey> immediatelyConnectedKeys;
    private ChannelBuilder channelBuilder;
    private final int maxReceiveSize;

    public Selector(int maxReceiveSize, ChannelBuilder channelBuilder) {
        try {
            this.nioSelector = java.nio.channels.Selector.open();
        } catch (IOException e) {
            throw new KafkaException(e);
        }

        this.maxReceiveSize = maxReceiveSize;
        this.channels = new HashMap<>();
        this.channelBuilder = channelBuilder;
        this.immediatelyConnectedKeys = new HashSet<>();
    }

    @Override
    public void connect(String id, InetSocketAddress address, int sendBufferSize, int receiverBufferSize) throws IOException {
        if (channels.containsKey(id)) {
            throw new IllegalStateException("There is already a connection for id " + id);
        }

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Socket socket = socketChannel.socket();
        socket.setKeepAlive(true);
        if (sendBufferSize != Selectable.USE_DEFAULT_BUFFER_SIZE) {
            socket.setSendBufferSize(sendBufferSize);
        }
        if (receiverBufferSize != Selectable.USE_DEFAULT_BUFFER_SIZE) {
            socket.setReceiveBufferSize(receiverBufferSize);
        }
        socket.setTcpNoDelay(true);
        boolean connected;
        try {
            connected = socketChannel.connect(address);
        } catch (IOException e) {
            socketChannel.close();
            throw e;
        }
        SelectionKey key = socketChannel.register(nioSelector, SelectionKey.OP_CONNECT);
        KafkaChannel channel = channelBuilder.buildChannel(id, key, maxReceiveSize);
        key.attach(channel);
        this.channels.put(id, channel);

        if (connected) {
            logger.debug("Immediately connectedto node {}", channel.getId());
            immediatelyConnectedKeys.add(key);
            key.interestOps(0);
        }
    }

    @Override
    public void wakeup() {

    }

    @Override
    public void close() {

    }

    @Override
    public void close(String id) {

    }

    @Override
    public KafkaChannel channel(String id) {
        return null;
    }
}