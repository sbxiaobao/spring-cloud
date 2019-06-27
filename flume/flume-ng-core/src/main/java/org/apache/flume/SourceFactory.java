package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public interface SourceFactory {

    Source create(String sourceName, String type);

    boolean unregister(Source source);
}
