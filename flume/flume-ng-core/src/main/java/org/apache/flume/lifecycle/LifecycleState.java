package org.apache.flume.lifecycle;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public enum LifecycleState {

    IDLE, START, STOP, ERROR;

    public static final LifecycleState[] START_OR_ERROR = new LifecycleState[]{
            START, ERROR
    };
    public static final LifecycleState[] stop_or_error = new LifecycleState[]{
            STOP, ERROR
    };
}