package org.apache.flume.lifecycle;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface LifecycleAware {

    void start();

    void stop();

    LifecycleState getLifecycleState();
}