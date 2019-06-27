package com.dianping.cat;

import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.internal.DefaultHeartbeat;
import com.dianping.cat.message.internal.DefaultMessageTree;
import com.dianping.cat.message.io.ChannelManager;
import com.dianping.cat.message.spi.MessageCodec;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.message.spi.codec.NativeMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/15
 */
public class CatClientMain {

    private static final Logger logger = LoggerFactory.getLogger(CatClientMain.class);

    private static MessageCodec nativeMessageCodec = new NativeMessageCodec();

    public static void main(String[] args) {
        Cat.checkAndInitialize();
//        ChannelManager channelManager = ChannelManager.getInstance();
//        Threads.forGroup("cat").start(channelManager);
//        ChannelFuture future = channelManager.channel();
//
//        if (future != null) {
//            for (int i = 0; i < 10; i++) {
//                MessageTree messageTree = new DefaultMessageTree();
//                Heartbeat h = new DefaultHeartbeat("demo", "demo");
//                h.addData("System", "system");
//                messageTree.setMessage(h);
//                ByteBuf buf = nativeMessageCodec.encode(messageTree);
//
//                future.channel().writeAndFlush(buf);
//            }
//        } else {
//            logger.error("Channel future is closed!");
//        }


    }
}