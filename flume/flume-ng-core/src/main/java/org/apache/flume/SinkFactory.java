package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public interface SinkFactory {

    Sink create(String name, String type) throws FlumeException;

    boolean unregister(Sink sink);
}