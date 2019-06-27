package org.apache.flume.channel;

import org.apache.flume.ChannelException;
import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public class JdbcChannel extends AbstractChannel {

    private static final Logger logger = LoggerFactory.getLogger(JdbcChannel.class);

    @Override
    public void put(Event event) throws ChannelException {

    }

    @Override
    public Event take() throws ChannelException {
        return null;
    }

    @Override
    public Transaction getTransaction() {
        return null;
    }
}
