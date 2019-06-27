package org.apache.flume.node.nodemanager;

import com.google.common.base.Preconditions;
import org.apache.flume.lifecycle.LifecycleAware;
import org.apache.flume.node.NodeManager;

import java.util.HashSet;
import java.util.Set;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public abstract class AbstractLogicalNodeManager implements NodeManager {

    private Set<LifecycleAware> nodes;

    public AbstractLogicalNodeManager() {
        nodes = new HashSet<>();
    }

    @Override
    public boolean add(LifecycleAware node) {
        Preconditions.checkNotNull(node);

        return nodes.add(node);
    }

    @Override
    public boolean remove(LifecycleAware node) {
        Preconditions.checkNotNull(node);

        return nodes.remove(node);
    }

    @Override
    public Set<LifecycleAware> getNodes() {
        return nodes;
    }

    @Override
    public void setNodes(Set<LifecycleAware> nodes) {
        Preconditions.checkNotNull(nodes);

        this.nodes = new HashSet<>(nodes);
    }

    @Override
    public String toString() {
        return "{ nodes:" + nodes + " }";
    }
}