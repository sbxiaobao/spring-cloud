package org.apache.flume.node;

import org.apache.flume.lifecycle.LifecycleAware;

import java.util.Set;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface NodeManager extends LifecycleAware {

    boolean add(LifecycleAware node);

    boolean remove(LifecycleAware node);

    Set<LifecycleAware> getNodes();

    void setNodes(Set<LifecycleAware> nodes);
}
