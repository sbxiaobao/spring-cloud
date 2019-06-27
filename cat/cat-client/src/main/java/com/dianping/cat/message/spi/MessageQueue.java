package com.dianping.cat.message.spi;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/8
 */
public interface MessageQueue {

    boolean offer(MessageTree tree);

    MessageTree peek();

    MessageTree poll();

    int size();
}