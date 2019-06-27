package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class EventDeliveryException extends Exception {

    public EventDeliveryException() {
        super();
    }

    public EventDeliveryException(String message) {
        super(message);
    }

    public EventDeliveryException(String message, Throwable t) {
        super(message, t);
    }

    public EventDeliveryException(Throwable t) {
        super(t);
    }
}
