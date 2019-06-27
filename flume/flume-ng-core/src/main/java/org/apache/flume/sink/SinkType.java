package org.apache.flume.sink;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public enum SinkType {

    OTHER(null),

    LOGGER(LoggerSink.class.getName()),

    HDFS("org.apache.flume.sink.hdfs.HDFSEventSink");

    private final String sinkClassName;

    private SinkType(String sinkClassName) {
        this.sinkClassName = sinkClassName;
    }

    public String getSinkClassName() {
        return sinkClassName;
    }
    }
