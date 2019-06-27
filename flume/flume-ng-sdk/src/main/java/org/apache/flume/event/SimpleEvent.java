package org.apache.flume.event;

import org.apache.flume.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class SimpleEvent implements Event {

    private Map<String, String> headers;
    private byte[] body;

    public SimpleEvent() {
        headers = new HashMap<>();
        body = null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{headers:" + headers + " body:" + body + " }";
    }
}