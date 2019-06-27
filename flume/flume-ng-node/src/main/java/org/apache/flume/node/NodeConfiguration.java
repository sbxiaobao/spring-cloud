package org.apache.flume.node;

import org.apache.flume.Channel;
import org.apache.flume.SinkRunner;
import org.apache.flume.SourceRunner;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface NodeConfiguration {

    Map<String, SourceRunner> getSourceRunners();

    Map<String, SinkRunner> getSinkRunners();

    Map<String, Channel> getChannels();
}