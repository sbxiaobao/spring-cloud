package org.apache.flume.source;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.flume.source.avro.AvroSourceProtocol;
import org.apache.flume.source.avro.Status;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public class TestAvroSource {

    @Test
    public void testRequest() throws IOException {
        boolean bound = false;

        AvroSourceProtocol client = SpecificRequestor.getClient(AvroSourceProtocol.class, new NettyTransceiver(new InetSocketAddress(4141)));
        for (int i = 0; i < 100 && !bound; i++) {
            AvroFlumeEvent event = new AvroFlumeEvent();

            event.setHeaders(new HashMap<CharSequence, CharSequence>());
            event.setBody(ByteBuffer.wrap("Hello avro".getBytes()));

            Status status = client.append(event);
        }
    }
}