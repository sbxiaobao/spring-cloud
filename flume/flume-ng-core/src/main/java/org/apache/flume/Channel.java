package org.apache.flume;

import org.apache.flume.lifecycle.LifecycleAware;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public interface Channel extends LifecycleAware, NamedComponent {

    void put(Event event) throws ChannelException;

    Event take() throws ChannelException;

    Transaction getTransaction();
}