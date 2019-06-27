package org.apache.flume.sink;

import org.apache.flume.Context;
import org.apache.flume.CounterGroup;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.conf.Configurable;
import org.apache.flume.formatter.output.PathManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStream;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public class RollingFileSink extends AbstractSink implements Configurable {

    private static final Logger logger = LoggerFactory.getLogger(RollingFileSink.class);

    private static final long defaultRollInterval = 30;

    private File directory;
    private long rollInterval;
    private OutputStream outputStream;
    private ScheduledExecutorService rollService;

    private CounterGroup counterGroup;
    private PathManager pathController;

    @Override
    public Status process() throws EventDeliveryException {
        return null;
    }

    @Override
    public void configure(Context context) {

    }
}
