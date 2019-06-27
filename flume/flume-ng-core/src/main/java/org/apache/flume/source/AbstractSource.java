package org.apache.flume.source;

import com.google.common.base.Preconditions;
import org.apache.flume.Source;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public abstract class AbstractSource implements Source {

    private ChannelProcessor channelProcessor;

    private String name;

    private LifecycleState lifecycleState;

    public AbstractSource() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public synchronized void start() {
//        Preconditions.checkState(channelProcessor != null, "No channel processor configured");

        lifecycleState = LifecycleState.START;
    }

    @Override
    public synchronized void stop() {
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public synchronized void setChannelProcessor(ChannelProcessor channelProcessor) {
        this.channelProcessor = channelProcessor;
    }

    @Override
    public synchronized ChannelProcessor getChannelProcessor() {
        return channelProcessor;
    }

    @Override
    public synchronized LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public synchronized String getName() {
        return name;
    }
}