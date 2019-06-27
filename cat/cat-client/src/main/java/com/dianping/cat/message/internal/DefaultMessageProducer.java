package com.dianping.cat.message.internal;

import com.dianping.cat.message.*;
import com.dianping.cat.message.spi.MessageManager;
import org.unidal.lookup.annotation.Named;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
@Named(type = MessageProducer.class)
public class DefaultMessageProducer implements MessageProducer {

    private static MessageProducer INSTANCE = new DefaultMessageProducer();
    private MessageManager manager = DefaultMessageManager.getInstance();

    @Override
    public Transaction newTransaction(String type, String name) {
        if (!manager.hasContext()) {
            manager.setup();
        }
        DefaultTransaction transaction = new DefaultTransaction(type, name, manager);

        return transaction;
    }

    public static MessageProducer getInstance() {
        return INSTANCE;
    }

    @Override
    public void logError(Throwable cause) {

    }

    @Override
    public void logError(String message, Throwable cause) {

    }

    @Override
    public void logEvent(String type, String name) {
        logEvent(type, name, Message.SUCCESS, null);
    }

    @Override
    public void logEvent(String type, String name, String status, String nameValuePairs) {
        Event event = newEvent(type, name);

        if (nameValuePairs != null && nameValuePairs.length() > 0) {
            event.addData(nameValuePairs);
        }

        event.setStatus(status);
        event.complete();
    }

    public Event newEvent(String type, String name) {
        if (!manager.hasContext()) {
            manager.setup();
        }

        return new DefaultEvent(type, name, manager);
    }

    @Override
    public Heartbeat newHeartbeat(String type, String name) {
        if (!manager.hasContext()) {
            manager.setup();
        }

        DefaultHeartbeat heartbeat = new DefaultHeartbeat(type, name, manager);

        manager.getThreadLocalMessageTree().setDiscardPrivate(false);
        return heartbeat;
    }
}
