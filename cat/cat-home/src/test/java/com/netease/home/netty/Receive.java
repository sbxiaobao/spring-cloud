package com.netease.home.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/11
 */
public class Receive {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public synchronized void startServer(int port) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(8);

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
//                        pipeline.addLast(new TimeServerHandler());
                    }
                });

        ChannelFuture f = bootstrap.bind(port).sync();
    }

    public static void main(String[] args) throws Exception {
        Receive receive = new Receive();
        receive.startServer(8080);
    }
}