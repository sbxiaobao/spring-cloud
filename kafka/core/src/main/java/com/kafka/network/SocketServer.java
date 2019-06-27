package com.kafka.network;

import com.kafka.cluster.EndPoint;
import com.kafka.common.KafkaException;
import com.kafka.common.network.KafkaChannel;
import com.kafka.common.utils.Time;
import com.kafka.server.KafkaConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private ConnectionQuotas connectionQuotas;
    private int maxConnectionPerIp = 100;
    private Map<String, Integer> maxConnectionPerIpOverrides = new HashMap<>();
    private KafkaConfig config;
    private Time time;

    public SocketServer(KafkaConfig config, Time time) {
        this.config = config;
        this.time = time;
        this.maxConnectionPerIp = config.MaxConnectionsPerIp;
    }

    public void startup() {
        synchronized (this) {
            connectionQuotas = new ConnectionQuotas(maxConnectionPerIp, maxConnectionPerIpOverrides);
            int sendBufferSize = 102400;
            int recvBufferSize = 65536;
            int brokerId = 0;

            int processorBeginIndex = 0;

        }
    }

    private abstract class AbstractServerThread implements Runnable {
        private CountDownLatch startupLatch = new CountDownLatch(1);
        private CountDownLatch shutdownLatch = new CountDownLatch(1);
        private AtomicBoolean alive = new AtomicBoolean(true);
        private ConnectionQuotas connectionQuotas;

        public AbstractServerThread(ConnectionQuotas connectionQuotas) {
            this.connectionQuotas = connectionQuotas;
        }

        public abstract void wakeup();

        public void shutdown() {
            alive.set(false);
            wakeup();
            try {
                shutdownLatch.await();
            } catch (InterruptedException e) {
                //ignore
            }
        }

        public void awaitStartup() {
            try {
                startupLatch.await();
            } catch (InterruptedException e) {
                //ignore
            }
        }

        protected void startupComplete() {
            startupLatch.countDown();
        }

        protected void shutdownComplete() {
            shutdownLatch.countDown();
        }

        protected boolean isRunning = alive.get();

        public void close(com.kafka.common.network.Selector selector, String connectionId) {
            KafkaChannel channel = selector.channel(connectionId);
            if (channel != null) {
                logger.debug("Closing selector connection {}", connectionId);
                InetAddress address = channel.socketAddress();
                if (address != null) {
                    connectionQuotas.dec(address);
                }
                selector.close();
            }
        }

        public void close(SocketChannel channel) {
            if (channel != null) {
                logger.debug("Closing connection from " + channel.socket().getRemoteSocketAddress());
                connectionQuotas.dec(channel.socket().getInetAddress());
                try {
                    channel.socket().close();
                    channel.close();
                } catch (IOException e) {
                    logger.error("Close channel error, {}", e.getMessage(), e);
                }
            }
        }
    }

    private class Acceptor extends AbstractServerThread {
        private Selector nioSelector;
        private ServerSocketChannel serverChannel;
        private Integer recvBufferSize;
        private EndPoint endPoint;
        private Integer sendBufferSize;
        private List<Processor> processors;

        public Acceptor(EndPoint endPoint, Integer sendBufferSize, Integer recvBufferSize, List<Processor> processors, ConnectionQuotas connectionQuotas) {
            super(connectionQuotas);
            this.sendBufferSize = sendBufferSize;
            this.recvBufferSize = recvBufferSize;
            this.processors = processors;
            this.endPoint = endPoint;
            try {
                nioSelector = Selector.open();
            } catch (IOException e) {
                //ignore
            }
            serverChannel = openServerSocket(endPoint.getHost(), endPoint.getPort());
        }

        @Override
        public void run() {
            try {
                serverChannel.register(nioSelector, SelectionKey.OP_ACCEPT);
                startupComplete();

                int currentProcessor = 0;
                while (isRunning) {
                    int ready = nioSelector.select(500);
                    if (ready > 0) {
                        Set<SelectionKey> keys = nioSelector.selectedKeys();
                        Iterator<SelectionKey> iter = keys.iterator();
                        while (iter.hasNext() && isRunning) {
                            SelectionKey key = iter.next();
                            iter.remove();
                            if (key.isAcceptable()) {
                                accept(key, processors.get(currentProcessor));
                            } else {
                                throw new IllegalStateException("Unrecognized key state for acceptor thread.");
                            }

                            currentProcessor = (currentProcessor + 1) % processors.size();
                        }
                    }
                }
            } catch (IOException e) {

            } finally {
                logger.debug("Closing server socket and selector.");
                try {
                    serverChannel.close();
                    nioSelector.close();
                    shutdownComplete();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        public void accept(SelectionKey key, Processor processor) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.socket().setTcpNoDelay(true);
                socketChannel.socket().setKeepAlive(true);
                socketChannel.socket().setSendBufferSize(sendBufferSize);

                logger.debug("Accepted connection from {} on {} and assigned it to processor {}, sendBufferSize [actual|requested] : [{}|{}] recvBufferSize [actual|requested]:[{}|{}]",
                        socketChannel.socket().getRemoteSocketAddress(), socketChannel.socket().getLocalAddress(), processor.id, socketChannel.socket().getReceiveBufferSize(), recvBufferSize);
                processor.accept(socketChannel);
            } catch (IOException e) {
                if (socketChannel != null) {
                    close(socketChannel);
                }
            }
        }

        private ServerSocketChannel openServerSocket(String host, int port) {
            InetSocketAddress socketAddress;
            if (StringUtils.isBlank(host)) {
                socketAddress = new InetSocketAddress(port);
            } else {
                socketAddress = new InetSocketAddress(host, port);
            }
            try {
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.socket().setReceiveBufferSize(recvBufferSize);
                serverChannel.socket().bind(socketAddress);
                logger.info("Awaiting socket connections on {}:{}", socketAddress.getHostString(), serverChannel.socket().getLocalPort());
            } catch (IOException e) {
                throw new KafkaException(String.format("Socket server failed to bind to %s:%d:%s", socketAddress.getHostString(), port, e.getMessage()), e);
            }
            return serverChannel;
        }

        @Override
        public void wakeup() {
            nioSelector.wakeup();
        }
    }

    private class Processor extends AbstractServerThread {
        private int id;

        public Processor(Integer id, ConnectionQuotas connectionQuotas) {
            super(connectionQuotas);
            this.id = id;
        }

        @Override
        public void wakeup() {

        }

        @Override
        public void run() {

        }

        public void accept(SocketChannel socketChannel) {
            wakeup();
        }
    }
}

class ConnectionQuotas {
    private int defaultMax;
    private Map<String, Integer> overrideQuotas;
    private Map<InetAddress, Integer> overrides;
    private Map<InetAddress, Integer> counts;

    public ConnectionQuotas(int defaultMax, Map<String, Integer> overrideQuotas) {
        this.defaultMax = defaultMax;
        this.overrideQuotas = overrideQuotas;

        counts = new HashMap<>();
        overrides = new HashMap<>();
        for (Map.Entry<String, Integer> entry : overrideQuotas.entrySet()) {
            String host = entry.getKey();
            Integer count = entry.getValue();

            try {
                overrides.put(InetAddress.getByName(host), count);
            } catch (UnknownHostException e) {
                //ignore
            }
        }
    }

    public void inc(InetAddress address) {
        synchronized (counts) {
            int count = counts.containsKey(address) ? counts.get(address) : 0;
            counts.put(address, count + 1);

            int max = overrides.containsKey(address) ? overrides.get(address) : defaultMax;
            if (count >= max) {
                throw new TooManyConnectionsException(address, max);
            }
        }
    }

    public void dec(InetAddress address) {
        synchronized (counts) {
            if (!counts.containsKey(address)) {
                throw new IllegalArgumentException(String.format("Attempted to decrease connection count for address with no connections, address:%s", address));
            }
            int count = counts.get(address);
            if (count == 1) {
                counts.remove(address);
            } else {
                counts.put(address, count - 1);
            }
        }
    }
}

class TooManyConnectionsException extends KafkaException {

    public TooManyConnectionsException(InetAddress ip, Integer count) {
        super(String.format(String.format("Too many connections from %s (maximun = %d)", ip, count)));
    }
}