package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageTree;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public interface MessageConsumer {

    void consume(MessageTree tree);

    void doCheckpoint();
}