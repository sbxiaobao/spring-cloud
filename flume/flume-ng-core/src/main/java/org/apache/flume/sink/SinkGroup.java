package org.apache.flume.sink;

import org.apache.flume.Context;
import org.apache.flume.Sink;
import org.apache.flume.SinkProcessor;
import org.apache.flume.conf.Configurable;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class SinkGroup implements Configurable {
    private static final String PRECESSOR_PREFIX = "processor.";
    List<Sink> sinks;
    SinkProcessor processor;

    public SinkGroup(List<Sink> groupSinks) {
        sinks = groupSinks;
    }

    @Override
    public void configure(Context context) {
        Context processorContext = new Context();
        Map<String, String> subparams = context.getSubProperties(PRECESSOR_PREFIX);
        processorContext.putAll(subparams);
        processor = SinkProcessorFactory.getProcessor(processorContext, sinks);
    }

    public SinkProcessor getProcessor() {
        return processor;
    }
}