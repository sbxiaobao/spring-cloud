package com.dianping.cat.message.io;


import com.dianping.cat.configuration.ClientConfigService;
import com.dianping.cat.configuration.DefaultClientConfigService;
import com.dianping.cat.configuration.MessageType;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageStatistics;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.internal.MessageIdFactory;
import com.dianping.cat.message.queue.DefaultMessageQueue;
import com.dianping.cat.message.queue.PriorityMessageQueue;
import com.dianping.cat.message.spi.MessageCodec;
import com.dianping.cat.message.spi.MessageQueue;
import com.dianping.cat.message.spi.MessageStatistics;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.message.spi.codec.NativeMessageCodec;
import com.dianping.cat.status.AbstraceCollector;
import com.dianping.cat.status.StatusExtensionRegister;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/2
 */
public class TcpSocketSender implements Threads.Task, MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(TcpSocketSender.class);

    private MessageCodec nativeCodec = new NativeMessageCodec();
    private MessageStatistics statistics = new DefaultMessageStatistics();
    private ChannelManager channelManager = ChannelManager.getInstance();
    private MessageQueue messageQueue = new PriorityMessageQueue(SIZE);
    private MessageIdFactory factory = MessageIdFactory.getInstance();
    private ClientConfigService configService = DefaultClientConfigService.getInstance();
    private AtomicMessageManager atomicQueueManager = new AtomicMessageManager(SIZE);

    private boolean active;
    private static final int SIZE = 5000;
    private static final long HOUR = 1000 * 60 * 60L;
    public static TcpSocketSender INSTANCE = new TcpSocketSender();
    private static final String ip = "127.0.0.1";
    private static final int port = 2280;

    public static TcpSocketSender getInstance() {
        return INSTANCE;
    }

    private TcpSocketSender() {
        InetSocketAddress address = new InetSocketAddress(ip, port);

        initialize(address);
    }

    private void initialize(InetSocketAddress address) {
        Threads.forGroup("cat").start(channelManager);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shut down cat client in runtime shut down hook!");
                shutdown();
            }
        });

        StatusExtensionRegister.getInstance().register(new AbstraceCollector() {
            @Override
            public String getId() {
                return "cat.status";
            }

            @Override
            public Map<String, String> getProperties() {
                Map<String, String> map = new LinkedHashMap<>();

                map.put("cat.status.send.sample.ratio", String.valueOf(300));
                map.put("cat.status.send.queue.size", String.valueOf(messageQueue.size()));
                map.put("cat.status.send.atomic.queue.size", String.valueOf(atomicQueueManager.getQueueSize()));

                Map<String, Long> values = statistics.getStatistics();

                for (Map.Entry<String, Long> entry : values.entrySet()) {
                    map.put(entry.getKey(), String.valueOf(entry.getValue()));
                }

                return map;
            }
        });
    }

    @Override
    public void send(MessageTree tree) {
        offer(tree);
    }

    private void offer(MessageTree tree) {
        MessageType type = configService.parseMessageType(tree);

        boolean result = true;
        switch (type) {
            case NORMAL_MESSAGE:
                result = messageQueue.offer(tree);
                break;
            case STAND_ALONE_EVENT:
                result = atomicQueueManager.offerToQueue(tree);
                break;
            default:
                break;
        }
        logger.info("Offer queueSize:{}, result:{}", messageQueue.size(), result);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void shutdown() {
        active = false;
        channelManager.shutdown();
    }

    private void processMessage() {
        ChannelFuture channel = channelManager.channel();

        if (channel != null) {
            MessageTree tree = null;

            try {
                tree = messageQueue.poll();

                if (tree != null) {
                    sendInternal(channel, tree);
                    tree.setMessage(null);
                } else {
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                        active = false;
                    }
                }
            } catch (Exception e) {
                logger.error("Error when sending message over TCP socket!", e);
            }
        } else {
            long current = System.currentTimeMillis();
            long oldTimestamp = current - HOUR;

            while (true) {
                try {
                    MessageTree tree = messageQueue.peek();

                    if (tree != null && tree.getMessage().getTimestamp() < oldTimestamp) {
                        MessageTree discardTree = messageQueue.poll();

                        if (discardTree != null) {
                            statistics.onOverflowed(discardTree);
                        }
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    break;
                }
            }

            try {
                Thread.sleep(5);
            } catch (Exception e) {
                active = false;
            }
        }
    }

    @Override
    public void run() {
        active = true;

        while (active) {
            processMessage();
            try {
//                Thread.sleep(3000L);
            } catch (Exception e) {
                //ignore
            }
//            atomicQueueManager.processAtomicMessage();
        }
    }

    private void sendInternal(ChannelFuture channel, MessageTree tree) {
        logger.info("Send internal", tree.getMessage());
        if (tree.getMessageId() == null) {
            tree.setMessageId(factory.getNextId());
        }

        ByteBuf buf = nativeCodec.encode(tree);
        int size = buf.readableBytes();

        channel.channel().writeAndFlush(buf);

        if (statistics != null) {
            statistics.onBytes(size);
        }
    }

    public class AtomicMessageManager {
        private MessageQueue smallMessages;
        private static final long HOUR = 1000 * 60 * 60L;
        private static final int MAX_CHILD_NUMBER = 200;
        private static final int MAX_DURATION = 1000 * 30;

        public AtomicMessageManager(int size) {
            smallMessages = new DefaultMessageQueue(size);
        }

        public int getQueueSize() {
            return smallMessages.size();
        }

        private boolean isSameHour(long time1, long time2) {
            int hour1 = (int) (time1 / HOUR);
            int hour2 = (int) (time2 / HOUR);

            return hour1 == hour2;
        }

        public boolean offerToQueue(MessageTree tree) {
            return smallMessages.offer(tree);
        }

        public void processAtomicMessage() {
            processNormalAtomicMessage();
        }

        void processNormalAtomicMessage() {
            while (true) {
                if (shouldMerge(smallMessages)) {
                    MessageTree tree = mergeTree(smallMessages);

                    offer(tree);
                } else {
                    break;
                }
            }
        }

        private MessageTree mergeTree(MessageQueue queue) {
            int max = MAX_CHILD_NUMBER;
            DefaultTransaction t = new DefaultTransaction("System", "AtomicAggregator");
            MessageTree first = queue.poll();
            final Message message = first.getMessage();
            final long timestamp = message.getTimestamp();

            t.setStatus(Transaction.SUCCESS);
            t.setCompleted(true);
            t.setDurationStart(timestamp);
            t.setDurationInMicro(0);
            t.addChild(message);

            while (max >= 0) {
                MessageTree tree = queue.peek();

                if (tree != null) {
                    long nextTimestamp = tree.getMessage().getTimestamp();

                    if (isSameHour(timestamp, nextTimestamp)) {
                        tree = queue.poll();

                        if (tree == null) {
                            break;
                        }
                        t.addChild(tree.getMessage());
                        max--;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            first.setMessage(t);
            return first;
        }

        private boolean shouldMerge(MessageQueue queue) {
            MessageTree tree = queue.peek();

            if (tree != null) {
                long firstTime = tree.getMessage().getTimestamp();
                return System.currentTimeMillis() - firstTime > MAX_DURATION || queue.size() >= MAX_CHILD_NUMBER;
            }
            return false;
        }
    }
}