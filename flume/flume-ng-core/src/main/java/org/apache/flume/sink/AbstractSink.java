package org.apache.flume.sink;

import com.google.common.base.Preconditions;
import org.apache.flume.Channel;
import org.apache.flume.Sink;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public abstract class AbstractSink implements Sink, LifecycleAware {

    private Channel channel;
    private String name;

    private LifecycleState lifecycleState;

    public AbstractSink() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void start() {
        Preconditions.checkNotNull(channel != null, "No channel configured");

        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
