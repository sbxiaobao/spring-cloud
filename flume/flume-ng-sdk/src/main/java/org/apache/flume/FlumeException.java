package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public class FlumeException extends RuntimeException {

    public FlumeException(String msg) {
        super(msg);
    }

    public FlumeException(String msg, Throwable t) {
        super(msg, t);
    }

    public FlumeException(Throwable t) {
        super(t);
    }
}
