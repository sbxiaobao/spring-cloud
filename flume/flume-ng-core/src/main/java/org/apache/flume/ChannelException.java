package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class ChannelException extends RuntimeException {

    public ChannelException(String message) {
        super(message);
    }

    public ChannelException(Throwable e) {
        super(e);
    }

    public ChannelException(String message, Throwable e) {
        super(message, e);
    }
}
