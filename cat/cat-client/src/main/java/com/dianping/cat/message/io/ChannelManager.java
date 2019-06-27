package com.dianping.cat.message.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/15
 */
public class ChannelManager implements Threads.Task {

    private static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    private static ChannelManager INSTANCE = new ChannelManager();
    private ChannelFuture channel;
    private Bootstrap bootstrap;
    private boolean active = true;
    private String host = "127.0.0.1";
    private int port = 2280;


    public static ChannelManager getInstance() {
        return INSTANCE;
    }

    private ChannelManager() {
        EventLoopGroup group = new NioEventLoopGroup(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);

                t.setDaemon(false);
                return t;
            }
        });

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
            }
        });
        this.bootstrap = bootstrap;
        channel = createChannel();
    }

    private ChannelFuture createChannel() {
        InetSocketAddress address = new InetSocketAddress(host, port);
        logger.info("Start connect server ", address.toString());
        ChannelFuture future;

        try {
            future = bootstrap.connect(address);
            future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS);
            if (future.isSuccess()) {
                logger.info("Success connect to server");
            } else {
                logger.error("Failed to connect to server");
            }
            return future;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getName() {
        return "netty-channel-health-check";
    }

    @Override
    public void shutdown() {
        this.active = false;
    }

    @Override
    public void run() {
        logger.info("Connect to server host:{}, port:{}", host, port);
    }

    public ChannelFuture channel() {
        if (checkWritable(channel)) {
            return channel;
        }
        return null;
    }

    private boolean checkWritable(ChannelFuture future) {
        boolean isWritable = false;
        if (future != null) {
            Channel channel = future.channel();

            if (channel.isActive() && channel.isOpen()) {
                if (channel.isWritable()) {
                    isWritable = true;
                } else {
                    channel.flush();
                }
            } else {
                logger.error("Channel buf is close when send msg!");
            }
        }
        return isWritable;
    }

    private class ConnectionListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            logger.info("Connecting listener");
            if (!channelFuture.isSuccess()) {
                logger.info("Connect to server failed, retry...");
                final EventLoop loop = channelFuture.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        createChannel();
                    }
                }, 1L, TimeUnit.SECONDS);
            }
        }
    }
}
