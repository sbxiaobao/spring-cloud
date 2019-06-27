package org.apache.flume.channel;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public enum ChannelSelectorType {

    OTHER(null),

    REPLICATING(ReplicatingChannelSelector.class.getName()),

    MULTIPLEXING(MultiplexingChannelSelector.class.getName());

    private final String channelSelectorClassName;

    private ChannelSelectorType(String channelSelectorClassName) {
        this.channelSelectorClassName = channelSelectorClassName;
    }

    public String getChannelSelectorClassName() {
        return channelSelectorClassName;
    }
}