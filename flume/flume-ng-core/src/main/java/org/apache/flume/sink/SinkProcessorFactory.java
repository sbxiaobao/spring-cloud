package org.apache.flume.sink;

import org.apache.flume.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public class SinkProcessorFactory {
    private static final Logger logger = LoggerFactory.getLogger(SinkProcessorFactory.class);

    private static final String TYPE = "type";

    public static SinkProcessor getProcessor(Context context, List<Sink> sinks) {
        Map<String, String> params = context.getParameters();
        SinkProcessor processor;
        String typeStr = params.get(TYPE);
        SinkProcessorType type = SinkProcessorType.DEFAULT;
        try {
            type = SinkProcessorType.valueOf(typeStr.toUpperCase());
        } catch (Exception e) {
            logger.warn("Sink type {} does not exist, using default", typeStr);
        }

        Class<? extends SinkProcessor> processorClass = null;
        try {
            processorClass = (Class<? extends SinkProcessor>) Class.forName(type.getSinkProcessorClassName());
        } catch (Exception e) {
            throw new FlumeException("Unable to load sink processor type: " + typeStr + ", class: " + type.getSinkProcessorClassName(), e);
        }
        try {
            processor = processorClass.newInstance();
        } catch (Exception e) {
            throw new FlumeException("Unable to load sink processor type: " + typeStr + ", class: " + type.getSinkProcessorClassName(), e);
        }

        processor.setSinks(sinks);
        processor.configure(context);
        return processor;
    }
}
