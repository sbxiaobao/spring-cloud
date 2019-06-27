package com.netease.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/11
 */
public class TimeClient implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TimeClient.class);

    public ChannelFuture f;
    private Bootstrap bootstrap;
    private static TimeClient INSTANCE = new TimeClient();

    public static TimeClient getInstance() {
        return INSTANCE;
    }

    private TimeClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) {
                        }
                    });
            this.bootstrap = bootstrap;
            String host = "127.0.0.1";
            int port = 8080;
            InetSocketAddress address = new InetSocketAddress(host, port);
            this.f = bootstrap.connect(address).sync();
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

//    public static void main(String[] args) {
//        TimeClient client = TimeClient.getInstance();
//    }
}
