package org.apache.flume.sink;

import com.google.common.base.Preconditions;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Sink;
import org.apache.flume.SinkProcessor;
import org.apache.flume.lifecycle.LifecycleState;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class DefaultSinkProcessor implements SinkProcessor {

    private Sink sink;
    private LifecycleState lifecycleState;

    @Override
    public Sink.Status process() throws EventDeliveryException {
        return sink.process();
    }

    @Override
    public void setSinks(List<Sink> sinks) {
        Preconditions.checkNotNull(sinks);
        Preconditions.checkArgument(sinks.size() == 1, "DefaultSinkPolicy can " + "only handl one sink, " +
                "try using a policy that supports multiple sinks");
        sink = sinks.get(0);
    }

    @Override
    public void configure(Context context) {
    }

    @Override
    public void start() {
        Preconditions.checkNotNull(sink, "DefaultSinkProcessor sink not set");
        sink.start();
        lifecycleState = LifecycleState.START;
    }

    @Override
    public void stop() {
        Preconditions.checkNotNull(sink, "DefaultSinkProcessor sink not set");
        sink.stop();
        lifecycleState = LifecycleState.STOP;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }
}
