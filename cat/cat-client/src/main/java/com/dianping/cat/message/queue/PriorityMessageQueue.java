package com.dianping.cat.message.queue;

import com.dianping.cat.message.spi.MessageQueue;
import com.dianping.cat.message.spi.MessageTree;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/8
 */
public class PriorityMessageQueue implements MessageQueue {

    private BlockingQueue<MessageTree> highQueue;
    private BlockingQueue<MessageTree> normalQueue;

    public PriorityMessageQueue(int size) {
        highQueue = new ArrayBlockingQueue<>(size / 2);
        normalQueue = new ArrayBlockingQueue<>(size);
    }

    @Override
    public boolean offer(MessageTree tree) {
        if (tree.canDiscard()) {
            return normalQueue.offer(tree);
        }
        return highQueue.offer(tree);
    }

    @Override
    public MessageTree peek() {
        MessageTree tree = highQueue.peek();

        if (tree == null) {
            tree = normalQueue.peek();
        }
        return tree;
    }

    @Override
    public MessageTree poll() {
        MessageTree tree = highQueue.poll();

        if (tree == null) {
            try {
                tree = normalQueue.poll(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return tree;
    }

    @Override
    public int size() {
        return normalQueue.size() + highQueue.size();
    }
}
