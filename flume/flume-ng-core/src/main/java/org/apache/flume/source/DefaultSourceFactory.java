package org.apache.flume.source;

import com.google.common.base.Preconditions;
import org.apache.flume.FlumeException;
import org.apache.flume.Source;
import org.apache.flume.SourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public class DefaultSourceFactory implements SourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSourceFactory.class);

    private final Map<Class<?>, Map<String, Source>> sources;

    public DefaultSourceFactory() {
        sources = new HashMap<>();
    }

    @Override
    public Source create(String sourceName, String type) {
        Preconditions.checkNotNull(sourceName);
        Preconditions.checkNotNull(type);

        logger.debug("Creating instance of source {}, type {}", sourceName, type);

        String sourceClassName = type;

        SourceType srcType = SourceType.OTHER;
        try {
            srcType = SourceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.debug("Source type {} is a custom type", type);
        }

        if (!srcType.equals(SourceType.OTHER)) {
            sourceClassName = srcType.getSourceClassName();
        }

        Class<? extends Source> sourceClass = null;
        try {
            sourceClass = (Class<? extends Source>) Class.forName(sourceClassName);
        } catch (Exception e) {
            throw new FlumeException("Unable to load source type: " + type + ", class: " + sourceClassName, e);
        }

        Map<String, Source> sourceMap = sources.get(sourceClass);
        if (sourceMap == null) {
            sourceMap = new HashMap<>();
            sources.put(sourceClass, sourceMap);
        }

        Source source = sourceMap.get(sourceName);

        if (source == null) {
            try {
                source = sourceClass.newInstance();
                source.setName(sourceName);
                sourceMap.put(sourceName, source);
            } catch (Exception e) {
                sources.remove(sourceClass);
                throw new FlumeException("Unable to create source: " + sourceName + ", type: " + type + ", class: " + sourceClassName, e);
            }

        }

        return source;
    }

    @Override
    public boolean unregister(Source source) {
        Preconditions.checkNotNull(source);
        boolean removed = false;

        logger.debug("Unregistering source {}", source);

        Map<String, Source> sourceMap = sources.get(source.getClass());
        if (sourceMap != null) {
            removed = (sourceMap.remove(source.getName()) != null);

            if (sourceMap.size() == 0) {
                sources.remove(source.getClass());
            }
        }
        return removed;
    }
}
