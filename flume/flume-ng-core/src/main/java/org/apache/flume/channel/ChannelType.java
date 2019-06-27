package org.apache.flume.channel;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public enum ChannelType {

    OTHER(null),

    MEMORY(MemoryChannel.class.getName()),

    JDBC("org.apache.flume.channel.jdbc.JdbcChannel");

    private final String channelClassName;

    private ChannelType(String channelClassName) {
        this.channelClassName = channelClassName;
    }

    public String getChannelClassName() {
        return channelClassName;
    }
}
