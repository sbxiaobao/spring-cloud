package com.netease.home.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/11
 */
public class ChannelManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    public ChannelFuture f;
    private Bootstrap bootstrap;
    private static ChannelManager INSTANCE = new ChannelManager();

    public static ChannelManager getInstance() {
        return INSTANCE;
    }

    private ChannelManager() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                        }
                    });
            this.bootstrap = b;
            String host = "127.0.0.1";
            int port = 8080;
            InetSocketAddress address = new InetSocketAddress(host, port);
            this.f = bootstrap.connect(address);
            this.f.awaitUninterruptibly();
            if (!f.isSuccess()) {
                logger.error("ChannelFuture error");
            } else {
                logger.info("Connect to server:{}:{}", host, port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public ChannelFuture channel() {
        boolean isWritable = false;
        if (f != null) {
            Channel channel = f.channel();
            if (channel.isActive() && channel.isOpen()) {
                isWritable = true;
            }
        }
        if (isWritable) {
            return f;
        }
        return null;
    }

    @Override
    public void run() {
        logger.info("Timeclient start...");
    }
}