package org.apache.flume.channel;

import com.google.common.base.Preconditions;
import org.apache.flume.Channel;
import org.apache.flume.ChannelFactory;
import org.apache.flume.FlumeException;
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
public class DefaultChannelFactory implements ChannelFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelFactory.class);

    private Map<Class<?>, Map<String, Channel>> channels;

    public DefaultChannelFactory() {
        channels = new HashMap<>();
    }

    @Override
    public Channel create(String name, String type) throws FlumeException {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        logger.debug("Creating instance of channel {} type {}", name, type);

        String channelClassName = type;

        ChannelType channelType = ChannelType.OTHER;

        try {
            channelType = channelType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.debug("Channel type {} is a custom type", type);
        }

        if (!channelType.equals(channelType.OTHER)) {
            channelClassName = channelType.getChannelClassName();
        }

        Class<? extends Channel> channelClass = null;
        try {
            channelClass = (Class<? extends Channel>) Class.forName(channelClassName);
        } catch (Exception e) {
            throw new FlumeException("Unable to load channel type: " + type + ", class: " + channelClassName, e);
        }

        Map<String, Channel> channelMap = channels.get(channelClass);
        if (channelMap == null) {
            channelMap = new HashMap<>();
            channels.put(channelClass, channelMap);
        }

        Channel channel = channelMap.get(name);

        if (channel == null) {
            try {
                channel = channelClass.newInstance();
                channel.setName(name);
                channelMap.put(name, channel);
            } catch (Exception e) {
                channels.remove(channelClass);
                throw new FlumeException("Unable to create channel: " + name + ", type: " + type + ", class: " + channelClassName, e);
            }
        }
        return channel;
    }

    @Override
    public boolean unregister(Channel channel) {
        Preconditions.checkNotNull(channel);
        logger.info("Unregister channel {}", channel);
        boolean removed = false;

        Map<String, Channel> channelMap = channels.get(channel.getClass());
        if (channelMap != null) {
            removed = (channelMap.remove(channel.getName()) != null);

            if (channelMap.size() == 0) {
                channels.remove(channel.getClass());
            }
        }
        return removed;
    }
}
