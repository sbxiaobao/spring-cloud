package org.apache.flume;

import org.apache.flume.sink.DefaultSinkProcessor;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public enum SinkProcessorType {

    DEFAULT(DefaultSinkProcessor.class.getName());

    private final String processorClassName;

    private SinkProcessorType(String processorClassName) {
        this.processorClassName = processorClassName;
    }

    public String getSinkProcessorClassName() {
        return processorClassName;
    }
}
