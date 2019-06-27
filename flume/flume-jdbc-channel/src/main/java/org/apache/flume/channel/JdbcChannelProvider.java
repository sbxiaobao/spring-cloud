package org.apache.flume.channel;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.Transaction;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public interface JdbcChannelProvider {

    void initialize(Context context);

    void close();

    void persistEvent(String channelName, Event event);

    Event removeEvent(String channelName);

    Transaction getTransaction();
}