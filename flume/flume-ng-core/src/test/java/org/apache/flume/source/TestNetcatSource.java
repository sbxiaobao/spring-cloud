package org.apache.flume.source;

import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/25
 */
public class TestNetcatSource {

    @Test
    public void testLifecycle() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable clientRequestRnnable = new Runnable() {
            @Override
            public void run() {
                try {
                    SocketChannel channel = SocketChannel.open(new InetSocketAddress(44444));
                    Writer writer = Channels.newWriter(channel, "utf-8");

                    writer.write("Test message");

                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 100; i++) {
            executor.submit(clientRequestRnnable);
        }
    }
}