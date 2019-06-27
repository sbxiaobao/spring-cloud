package org.apache.flume.channel;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurables;
import org.apache.flume.event.EventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/24
 */
public class TestMemoryChannel {

    private Channel channel;

    @Before
    public void setUp() {
        channel = new MemoryChannel();
    }

    @Test
    public void testPutTake() {
        Event event = EventBuilder.withBody("test event".getBytes());
        Context context = new Context();

        Configurables.configure(channel, context);

        Transaction transaction = channel.getTransaction();
        transaction.begin();
        channel.put(event);
        transaction.commit();
        transaction.close();

//        transaction = channel.getTransaction();

//        transaction.begin();
//        channel.take();
//        transaction.commit();
    }

    @Test
    public void testChannelResize() {
        Context context = new Context();
        Map<String, String> params = new HashMap<>();
        params.put("capacity", "5");
        params.put("transactionCapacity", "5");
        context.putAll(params);
        Configurables.configure(channel, context);

        Transaction transaction = channel.getTransaction();
        transaction.begin();
        for (int i = 0; i < 5; i++) {
            channel.put(EventBuilder.withBody(String.format("test event %d", i).getBytes()));
        }
        transaction.commit();
        transaction.close();
    }

    @Test
    public void testSemaphore() throws InterruptedException{
        Semaphore queueStored = new Semaphore(0);
        queueStored.release(1);

        System.out.println(queueStored.availablePermits());
        System.out.println(queueStored.tryAcquire(3, TimeUnit.SECONDS));
        System.out.println(queueStored.availablePermits());

        System.out.println(queueStored.tryAcquire(3, TimeUnit.SECONDS));
    }
}