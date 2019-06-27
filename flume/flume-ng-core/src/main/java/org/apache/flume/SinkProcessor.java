package org.apache.flume;

import org.apache.flume.conf.Configurable;
import org.apache.flume.lifecycle.LifecycleAware;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public interface SinkProcessor extends LifecycleAware, Configurable {

    Sink.Status process() throws EventDeliveryException;

    void setSinks(List<Sink> sinks);
}