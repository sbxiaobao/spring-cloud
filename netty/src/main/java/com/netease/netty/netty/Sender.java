package com.netease.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/11
 */
public class Sender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private TimeClient timeClient = TimeClient.getInstance();

    public static final Sender INSTANCE = new Sender();

    public static Sender getInstance() {
        return INSTANCE;
    }

    private Sender() {
        init();
    }

    private void init() {
        new Thread(timeClient).start();
    }

    @Override
    public void run() {
        logger.info("Sender start...");
        while (true) {
            if (timeClient == null || timeClient.channel() == null) {
                continue;
            }
            byte[] req = "hello".getBytes();
            ByteBuf hello = Unpooled.buffer(req.length);
            hello.writeBytes(req);

            logger.info("Channel is open :{}, channel is writeable:{}", timeClient.f.channel().isOpen(), timeClient.f.channel().isWritable());
            timeClient.f.channel().writeAndFlush(hello);
            break;
        }
    }

    public static void main(String[] args) {
        Sender sender = Sender.getInstance();
        new Thread(sender).start();
    }
}
