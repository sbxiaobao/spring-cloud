package org.apache.flume.node.nodemanager;

import org.apache.flume.node.NodeConfiguration;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface NodeConfigurationAware {

    void onNodeConfigurationChanged(NodeConfiguration nodeConfiguration);
}
