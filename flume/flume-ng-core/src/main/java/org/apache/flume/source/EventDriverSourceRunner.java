package org.apache.flume.source;

import org.apache.flume.SourceRunner;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class EventDriverSourceRunner extends SourceRunner {

    private LifecycleState lifecycleState;

    public EventDriverSourceRunner() {
        lifecycleState = LifecycleState.IDLE;
    }

    @Override
    public void start() {
        getSource().start();
        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        getSource().stop();
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public String toString() {
        return "EventDrivenSourceRunner: {source:" + getSource() + " }";
    }
}