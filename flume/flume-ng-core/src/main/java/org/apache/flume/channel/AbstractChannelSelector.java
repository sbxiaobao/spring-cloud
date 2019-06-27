package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.ChannelSelector;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public abstract class AbstractChannelSelector implements ChannelSelector {

    private List<Channel> channels;

    private String name;

    @Override
    public List<Channel> getAllChannels() {
        return channels;
    }

    @Override
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public synchronized String getName() {
        return name;
    }
}
