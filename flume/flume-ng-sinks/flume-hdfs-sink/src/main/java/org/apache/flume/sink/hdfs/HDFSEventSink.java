package org.apache.flume.sink.hdfs;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public class HDFSEventSink extends AbstractSink implements Configurable {

    private static final Logger logger = LoggerFactory.getLogger(HDFSEventSink.class);

    @Override
    public Status process() throws EventDeliveryException {
        return null;
    }

    @Override
    public void configure(Context context) {

    }
}
