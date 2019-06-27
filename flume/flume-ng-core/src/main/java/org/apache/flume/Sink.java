package org.apache.flume;

import org.apache.flume.lifecycle.LifecycleAware;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public interface Sink extends LifecycleAware, NamedComponent {

    void setChannel(Channel channel);

    Channel getChannel();

    Status process() throws EventDeliveryException;

    public static enum Status {
        READY, BACKOFF;
    }
}
