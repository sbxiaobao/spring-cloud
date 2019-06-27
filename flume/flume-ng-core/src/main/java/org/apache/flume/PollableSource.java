package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface PollableSource extends Source {

    Status process();

    public static enum Status {
        READY, BACKOFF
    }
}