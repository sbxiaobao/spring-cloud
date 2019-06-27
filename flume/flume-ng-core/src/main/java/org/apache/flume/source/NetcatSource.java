package org.apache.flume.source;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.Configurables;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public class NetcatSource extends AbstractSource implements Configurable, EventDriverSource {

    private static final Logger logger = LoggerFactory.getLogger(NetcatSource.class);

    private String hostName;
    private int port;

    private CounterGroup counterGroup;
    private ServerSocketChannel serverSocket;
    private AtomicBoolean acceptThreadShouldStop;
    private Thread acceptThread;
    private ExecutorService handlerService;

    public NetcatSource() {
        super();

        port = 0;
        counterGroup = new CounterGroup();
        acceptThreadShouldStop = new AtomicBoolean();
    }

    @Override
    public void configure(Context context) {
        Configurables.ensureOptionalNonNull(context, "bind", "port");

        hostName = context.getString("bind");
        port = Integer.parseInt(context.getString("port"));
    }

    @Override
    public void start() {
        logger.info("Source starting");

        super.start();

        counterGroup.incrementAndGet("open.attempts");

        handlerService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("netcat-handler-%d").build());

        SocketAddress bindPoint = new InetSocketAddress(hostName, port);

        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().setReuseAddress(true);
            serverSocket.socket().bind(bindPoint);

            logger.info("Created serverSocket:{}", serverSocket);
        } catch (IOException e) {
            counterGroup.incrementAndGet("open.errors");
            logger.error("Unable to bind to socket. Exception follows.", e);
            return;
        }

        AcceptHandler acceptRunnable = new AcceptHandler();

        acceptRunnable.counterGroup = counterGroup;
        acceptRunnable.handlerService = handlerService;
        acceptRunnable.shouldStop = acceptThreadShouldStop;
        acceptRunnable.source = this;
        acceptRunnable.serverSocket = serverSocket;

        acceptThread = new Thread(acceptRunnable);

        acceptThread.start();

        logger.debug("Source started");
    }

    @Override
    public void stop() {
        logger.info("Souce stopping");

        super.stop();

        acceptThreadShouldStop.set(true);

        if (acceptThread != null) {
            logger.debug("Stopping accept handler thread");

            while (acceptThread.isAlive()) {
                try {
                    logger.debug("Waiting for accept handler to finish");
                    acceptThread.interrupt();
                    acceptThread.join(500);
                } catch (InterruptedException e) {
                    logger.debug("Interrupted while waiting for accept handler to finish");
                    Thread.currentThread().interrupt();
                }
            }

            logger.debug("Stopped accept handler thread");
        }

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error("Unable to close socket. Exceptions follows", e);
                return;
            }
        }

        if (handlerService != null) {
            handlerService.shutdown();

            while (!handlerService.isTerminated()) {
                logger.debug("Waiting for handler service to stop");
                try {
                    handlerService.awaitTermination(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    logger.debug("Interrupted while waiting for netcat handler service to stop");
                    handlerService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }

            logger.debug("Handler service stopped");
        }

        logger.debug("Source stopped, Event metrics:{}", counterGroup);
    }

    public static class AcceptHandler implements Runnable {

        private ServerSocketChannel serverSocket;
        private CounterGroup counterGroup;
        private ExecutorService handlerService;
        private EventDriverSource source;

        private AtomicBoolean shouldStop;

        @Override
        public void run() {
            logger.debug("Starting accept handler");

            while (!shouldStop.get()) {
                try {
                    SocketChannel socketChannel = serverSocket.accept();

                    NetcatSocketHanlder request = new NetcatSocketHanlder();

                    request.socketChannel = socketChannel;
                    request.counterGroup = counterGroup;
                    request.source = source;

                    handlerService.submit(request);

                    counterGroup.incrementAndGet("accept.succeeded");
                } catch (ClosedByInterruptException e) {
                } catch (IOException e) {
                    logger.error("Unable to accept connection. Exception follows.", e);
                    counterGroup.incrementAndGet("accept.failed");
                }
            }
            logger.debug("Accept handler exiting");
        }
    }

    public static class NetcatSocketHanlder implements Runnable {

        private Source source;

        private CounterGroup counterGroup;
        private SocketChannel socketChannel;

        @Override
        public void run() {
            Event event = null;

            try {
                Reader reader = Channels.newReader(socketChannel, "utf-8");
                Writer writer = Channels.newWriter(socketChannel, "utf-8");
                CharBuffer buffer = CharBuffer.allocate(512);
                StringBuilder builder = new StringBuilder();

                while (reader.read(buffer) != -1) {
                    buffer.flip();

                    logger.debug("read {} characters", buffer.remaining());

                    counterGroup.addAndGet("characters.received", Long.valueOf(buffer.limit()));
                    builder.append(buffer.array(), buffer.position(), buffer.length());
                }

                if (builder.charAt(buffer.length() - 1) == '\n') {
                    builder.deleteCharAt(buffer.length() - 1);
                }

                event = EventBuilder.withBody(buffer.toString().getBytes());
                Exception ex = null;

                try {
                    source.getChannelProcessor().processEvent(event);
                } catch (ChannelException chEx) {
                    ex = chEx;
                }

                if (ex == null) {
                    writer.append("OK\n");
                } else {
                    writer.append("FAILED: " + ex.getMessage() + "\n");
                }

                socketChannel.close();

                counterGroup.incrementAndGet("events.success");
            } catch (IOException e) {
                counterGroup.incrementAndGet("events.failed");
            }
        }
    }
}