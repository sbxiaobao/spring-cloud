package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public abstract class AbstractChannel implements Channel, LifecycleAware, Configurable {

    private String name;

    private LifecycleState lifecycleState;

    public AbstractChannel() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void start() {
        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public void configure(Context context) {

    }
}
