package org.apache.flume;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public interface Event {

    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> headers);

    byte[] getBody();

    void setBody(byte[] body);
}
