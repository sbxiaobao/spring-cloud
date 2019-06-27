package org.apache.flume;

import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.lifecycle.LifecycleAware;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface Source extends LifecycleAware, NamedComponent {

    void setChannelProcessor(ChannelProcessor channelProcessor);

    ChannelProcessor getChannelProcessor();
}