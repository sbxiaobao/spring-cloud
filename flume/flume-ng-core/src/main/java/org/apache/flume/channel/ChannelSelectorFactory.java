package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;
import org.apache.flume.Context;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.Configurables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class ChannelSelectorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ChannelSelectorFactory.class);

    public static ChannelSelector create(List<Channel> channels, Map<String, String> config) {
        ChannelSelector selector = getSelectForType(config.get("type"));

        selector.setChannels(channels);

        Context context = new Context();
        config.putAll(config);

        Configurables.configure(selector, context);

        return selector;
    }

    private static ChannelSelector getSelectForType(String type) {
        if (type == null || type.trim().length() == 0) {
            return new ReplicatingChannelSelector();
        }

        String selectorClassName = type;
        ChannelSelectorType selectorType = ChannelSelectorType.OTHER;

        try {
            selectorType = ChannelSelectorType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.debug("Selector type {} is a custom type", type);
        }

        if (!selectorType.equals(ChannelSelectorType.OTHER)) {
            selectorClassName = selectorType.getChannelSelectorClassName();
        }

        ChannelSelector selector = null;

        try {
            Class<? extends ChannelSelector> selectorClass = (Class<? extends ChannelSelector>) Class.forName(selectorClassName);
            selector = selectorClass.newInstance();
        } catch (Exception e) {
            throw new FlumeException("Unable to load selector type: " + type + ", class: " + selectorClassName, e);
        }

        return selector;
    }
}