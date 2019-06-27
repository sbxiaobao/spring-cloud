package com.dianping.cat.message.internal;

import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageManager;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public class DefaultTransaction extends AbstractMessage implements Transaction {
    private long durationInMicro = -1;
    private long durationStart;
    private List<Message> children;
    private MessageManager manager;

    public DefaultTransaction(String type, String name) {
        super(type, name);
        durationStart = System.nanoTime();
    }

    public DefaultTransaction(String type, String name, MessageManager manager) {
        super(type, name);
        this.manager = manager;

        durationStart = System.nanoTime();
    }

    @Override
    public Transaction addChild(Message message) {
        return null;
    }

    public void setDurationStart(long durationStart) {
        this.durationStart = durationStart;
    }

    public void setDurationInMicro(long durationInMicro) {
        this.durationInMicro = durationInMicro;
    }

    @Override
    public void complete() {
        try {
            if (isCompleted()) {

            } else {
                if (durationInMicro == -1) {
                    durationInMicro = (System.nanoTime() - durationStart) / 1000L;
                }
                setCompleted(true);

                if (manager != null) {
                    manager.getThreadLocalMessageTree().setDiscardPrivate(false);
                }

                if (manager != null) {
                    manager.end(this);
                }
            }
        } catch (Exception e) {
            //ignore
        }
    }
}