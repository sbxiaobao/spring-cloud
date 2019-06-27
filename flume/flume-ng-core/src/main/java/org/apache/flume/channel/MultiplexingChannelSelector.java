package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class MultiplexingChannelSelector extends AbstractChannelSelector {

    public static final String CONFIG_MULTIPLEX_HEADER_NAME = "header";
    public static final String DEFAULT_MULTIPLEX_HEADER = "flume.selector.header";
    public static final String CONFIG_PREFIX_MAPPING = "mapping";
    public static final String CONFIG_PREFIX_DEFAULT = "default";

    private static final Logger logger = LoggerFactory.getLogger(MultiplexingChannelSelector.class);

    private static final List<Channel> EMPTY_LIST = Collections.emptyList();

    private String headerName;

    private Map<String, List<Channel>> channelMapping;

    private List<Channel> defaultChannels;

    @Override
    public List<Channel> getRequiredChannles(Event event) {
        String headerValue = event.getHeaders().get(headerName);
        if (headerValue == null || headerValue.trim().length() == 0) {
            return defaultChannels;
        }

        List<Channel> channels = channelMapping.get(headerValue);

        if (channels == null) {
            channels = defaultChannels;
        }

        return channels;
    }

    @Override
    public List<Channel> getOptionalChannels(Event event) {
        return null;
    }

    @Override
    public void configure(Context context) {
        this.headerName = context.getString(CONFIG_MULTIPLEX_HEADER_NAME, DEFAULT_MULTIPLEX_HEADER);

        Map<String, Channel> channelNameMap = new HashMap<>();
        for (Channel ch : getAllChannels()) {
            channelNameMap.put(ch.getName(), ch);
        }

        defaultChannels = getChannelListFromNames(context.getString(CONFIG_PREFIX_DEFAULT), channelNameMap);

        if (defaultChannels.isEmpty()) {
            throw new FlumeException("Default channel list empty");
        }

        Map<String, String> mapConfig = context.getSubProperties(CONFIG_PREFIX_MAPPING + ".");

        for (String headerValue : mapConfig.keySet()) {
            List<Channel> configuredChannels = getChannelListFromNames(mapConfig.get(headerValue), channelNameMap);

            if (configuredChannels.size() == 0) {
                throw new FlumeException("No channel configured for when " + "header value is: " + headerValue);
            }

            if (channelMapping.put(headerValue, configuredChannels) != null) {
                throw new FlumeException("Selector channel configured twice");
            }
        }
    }

    private List<Channel> getChannelListFromNames(String channels, Map<String, Channel> channelNameMap) {
        List<Channel> configuredChannels = new ArrayList<>();
        String[] chNames = channels.split(" ");
        for (String name : chNames) {
            Channel ch = channelNameMap.get(name);
            if (ch != null) {
                configuredChannels.add(ch);
            } else {
                throw new FlumeException("Selector channel not found: " + name);
            }
        }
        return configuredChannels;
    }
}