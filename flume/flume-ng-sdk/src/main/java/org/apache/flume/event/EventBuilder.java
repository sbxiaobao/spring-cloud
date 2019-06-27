package org.apache.flume.event;

import org.apache.flume.Event;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class EventBuilder {

    public static Event withBody(byte[] body, Map<String, String> headers) {
        Event event = new SimpleEvent();

        event.setBody(body);

        if (headers != null) {
            event.setHeaders(new HashMap<>(headers));
        }

        return event;
    }

    public static Event withBody(byte[] body) {
        return withBody(body, null);
    }

    public static Event withBody(String body, Charset charset, Map<String, String> headers) {
        return withBody(body.getBytes(charset), headers);
    }

    public static Event withBody(String body, Charset charset) {
        return withBody(body, charset, null);
    }
}