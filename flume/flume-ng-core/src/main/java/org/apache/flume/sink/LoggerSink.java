package org.apache.flume.sink;

import org.apache.flume.Channel;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class LoggerSink extends AbstractSink {

    private static final Logger logger = LoggerFactory.getLogger(LoggerSink.class);

    @Override
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event = null;

        try {
            transaction.begin();
            event = channel.take();

            if (event != null) {
                if (logger.isInfoEnabled()) {
                    logger.info("Event: " + event);
                }
            } else {
                result = Status.BACKOFF;
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new EventDeliveryException("Failed to log event: " + event, e);
        } finally {
            transaction.close();
        }
        return result;
    }
}