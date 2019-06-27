package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;

import java.util.Collections;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class ReplicatingChannelSelector extends AbstractChannelSelector {

    private final List<Channel> emptyList = Collections.emptyList();

    @Override
    public List<Channel> getRequiredChannles(Event event) {
        return getAllChannels();
    }

    @Override
    public List<Channel> getOptionalChannels(Event event) {
        return emptyList;
    }

    @Override
    public void configure(Context context) {
    }
}
