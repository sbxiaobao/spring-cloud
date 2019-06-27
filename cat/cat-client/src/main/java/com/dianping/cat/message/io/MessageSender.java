package com.dianping.cat.message.io;

import com.dianping.cat.message.spi.MessageTree;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/29
 */
public interface MessageSender {

    void send(MessageTree tree);

    void shutdown();
}
