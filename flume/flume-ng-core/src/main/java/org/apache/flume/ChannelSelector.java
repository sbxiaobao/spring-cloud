package org.apache.flume;

import org.apache.flume.conf.Configurable;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public interface ChannelSelector extends NamedComponent, Configurable {

    void setChannels(List<Channel> channels);

    List<Channel> getRequiredChannles(Event event);

    List<Channel> getOptionalChannels(Event event);

    List<Channel> getAllChannels();
}