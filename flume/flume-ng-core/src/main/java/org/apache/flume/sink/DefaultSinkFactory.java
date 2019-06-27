package org.apache.flume.sink;

import com.google.common.base.Preconditions;
import org.apache.flume.FlumeException;
import org.apache.flume.Sink;
import org.apache.flume.SinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class DefaultSinkFactory implements SinkFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSinkFactory.class);

    private final Map<Class<?>, Map<String, Sink>> sinks;

    public DefaultSinkFactory() {
        sinks = new HashMap<>();
    }

    @Override
    public Sink create(String name, String type) throws FlumeException {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        logger.info("Creating instance of sink {} type {}", name, type);

        String sinkClassName = type;

        SinkType sinkType = SinkType.OTHER;
        try {
            sinkType = SinkType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.debug("Sink type {} is a custom type", type);
        }

        if (!sinkType.equals(SinkType.OTHER)) {
            sinkClassName = sinkType.getSinkClassName();
        }

        Class<? extends Sink> sinkClass = null;
        try {
            sinkClass = (Class<? extends Sink>) Class.forName(sinkClassName);
        } catch (Exception e) {
            throw new FlumeException("Unable to load sink type: " + type + ", class: " + sinkClassName, e);
        }

        Map<String, Sink> sinkMap = sinks.get(sinkClass);
        if (sinkMap == null) {
            sinkMap = new HashMap<>();
            sinks.put(sinkClass, sinkMap);
        }

        Sink sink = sinkMap.get(name);

        if (sink == null) {
            try {
                sink = sinkClass.newInstance();
                sink.setName(name);
                sinkMap.put(name, sink);
            } catch (Exception e) {
                sinks.remove(sinkClass);
                throw new FlumeException("Unable to create sink: " + name + ", type: " + type + ", class: " + sinkClassName, e);
            }
        }

        return sink;
    }

    @Override
    public boolean unregister(Sink sink) {
        Preconditions.checkNotNull(sink);
        boolean removed = false;

        logger.debug("Unregistering sink {}", sink);

        Map<String, Sink> sinkMap = sinks.get(sink.getClass());
        if (sinkMap != null) {
            removed = sinkMap.remove(sink.getName()) != null;

            if (sinkMap.size() == 0) {
                sinks.remove(sink.getClass());
            }
        }

        return removed;
    }
}