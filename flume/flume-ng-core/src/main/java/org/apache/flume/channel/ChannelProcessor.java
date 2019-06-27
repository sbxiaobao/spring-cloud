package org.apache.flume.channel;

import org.apache.flume.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class ChannelProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ChannelProcessor.class);

    private ChannelSelector selector;

    public ChannelProcessor(ChannelSelector selector) {
        this.selector = selector;
    }

    public ChannelSelector getSelector() {
        return selector;
    }

    public void processEvent(Event event) {
        List<Channel> requiredChannels = selector.getRequiredChannles(event);
        for (Channel requiredChannel : requiredChannels) {
            Transaction tx = null;
            try {
                tx = requiredChannel.getTransaction();
                tx.begin();

                requiredChannel.put(event);

                tx.commit();
            } catch (ChannelException ex) {
                tx.rollback();
            } finally {
                if (tx != null) {
                    tx.close();
                }
            }
        }

        List<Channel> optionalChannels = selector.getOptionalChannels(event);
        for (Channel optionalChannel : optionalChannels) {
            Transaction tx = null;
            try {
                tx = optionalChannel.getTransaction();
                tx.begin();

                optionalChannel.put(event);

                tx.commit();
            } catch (ChannelException e) {
                tx.rollback();
                logger.warn("Unable to put event on optional channel " + optionalChannel.getName(), e);
            } finally {
                if (tx != null) {
                    tx.close();
                }
            }
        }
    }

    public void processEventBatch(List<Event> events) {
        for (Event event : events) {
            System.out.println(event);
        }
    }
}