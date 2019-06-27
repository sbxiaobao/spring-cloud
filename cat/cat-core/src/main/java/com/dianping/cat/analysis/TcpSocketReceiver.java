package com.dianping.cat.analysis;

import com.dianping.cat.CatConstants;
import com.dianping.cat.message.CodecHandler;
import com.dianping.cat.message.internal.DefaultMessageTree;
import com.dianping.cat.message.io.ClientMessageEncoder;
import com.dianping.cat.message.io.TcpSocketSender;
import com.dianping.cat.statistic.ServerStatisticManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/8
 */
public class TcpSocketReceiver {

    private static final Logger logger = LoggerFactory.getLogger(TcpSocketSender.class);

    private ChannelFuture future;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerStatisticManager serverStatisticManager = ServerStatisticManager.getInstance();

    private MessageHandler handler = DefaultMessageHandler.getInstance();

    private int port = 2280;

    public synchronized void startServer(int port) {
        int threads = 24;
        ServerBootstrap bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(threads);
        workerGroup = new NioEventLoopGroup(threads);
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                ChannelPipeline pipeline = channel.pipeline();

                pipeline.addLast("decode", new MessageDecoder());
                pipeline.addLast("encode", new ClientMessageEncoder());
            }
        });

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        try {
            future = bootstrap.bind(port).sync();
            logger.info("Start netty server");
        } catch (Exception e) {
            //ignore
        }
    }

    public void init() {
        startServer(port);
    }

    public synchronized void destory() {
        logger.info("Start shutdown socket, port", port);
        future.channel().closeFuture();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logger.info("Shutdown socket success");
    }

    public class MessageDecoder extends ByteToMessageDecoder {
        private long processCount;

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) {
            if (byteBuf.readableBytes() < 4) {
                return;
            }
            byteBuf.markReaderIndex();
            int length = byteBuf.readInt();
            byteBuf.resetReaderIndex();
            if (byteBuf.readableBytes() < length + 4) {
                return;
            }
            try {
                if (length > 0) {
                    ByteBuf readBytes = byteBuf.readBytes(length + 4);

                    readBytes.markReaderIndex();

                    DefaultMessageTree tree = (DefaultMessageTree) CodecHandler.decode(readBytes);

                    readBytes.resetReaderIndex();
                    tree.setBuffer(readBytes);
                    handler.handle(tree);
                    processCount++;

                    long flag = processCount % CatConstants.SUCCESS_COUNT;

                    if (flag == 0) {
                        serverStatisticManager.addMessageTotalLoss(CatConstants.SUCCESS_COUNT);
                    }
                } else {
                    // client message is error
                    byteBuf.readBytes(length);
                    BufReleseHelper.release(byteBuf);
                }
            } catch (Exception e) {
                serverStatisticManager.addMessageTotalLoss(1);
                logger.error(e.getMessage(), e);
            }
        }
    }
}
