package org.apache.flume.conf.file;

import org.apache.flume.Channel;
import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;
import org.apache.flume.node.NodeConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public class SimpleNodeConfiguration implements NodeConfiguration {

    private Map<String, SourceRunner> sourceRunners;
    private Map<String, Channel> channels;
    private Map<String, SinkRunner> sinkRunners;

    public SimpleNodeConfiguration() {
        sourceRunners = new HashMap<>();
        channels = new HashMap<>();
        sinkRunners = new HashMap<>();
    }

    @Override
    public Map<String, SourceRunner> getSourceRunners() {
        return sourceRunners;
    }

    @Override
    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public Map<String, SinkRunner> getSinkRunners() {
        return sinkRunners;
    }
}