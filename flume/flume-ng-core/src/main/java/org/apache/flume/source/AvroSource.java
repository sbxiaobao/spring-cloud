package org.apache.flume.source;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Responder;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.flume.Context;
import org.apache.flume.CounterGroup;
import org.apache.flume.Event;
import org.apache.flume.EventDriverSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.apache.flume.source.avro.AvroSourceProtocol;
import org.apache.flume.source.avro.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/17
 */
public class AvroSource extends AbstractSource implements EventDriverSource, AvroSourceProtocol, Configurable {

    private static final Logger logger = LoggerFactory.getLogger(AvroSource.class);

    private int port;

    private String bindAddress;

    private Server server;

    private CounterGroup counterGroup;

    public AvroSource() {
        this.counterGroup = new CounterGroup();
    }

    @Override
    public void configure(Context context) {
        port = Integer.parseInt(context.getString("port"));
        bindAddress = context.getString("bind");
    }

    @Override
    public void start() {
        logger.info("Avro source starting:{}", this);

        Responder responder = new SpecificResponder(AvroSourceProtocol.class, this);
        server = new NettyServer(responder, new InetSocketAddress(bindAddress, port));

        server.start();

        super.start();

        logger.debug("Avro source started.");
    }

    @Override
    public Status append(AvroFlumeEvent avroEvent) throws AvroRemoteException {
        logger.debug("Receviced avro event:{}", avroEvent);

        counterGroup.incrementAndGet("rpc.received");

        Event event = EventBuilder.withBody(avroEvent.getBody().array(), toStringMap(avroEvent.getHeaders()));

        try {
            getChannelProcessor().processEvent(event);
        } catch (Exception e) {
            return Status.FAILED;
        }

        counterGroup.incrementAndGet("rpc.successful");
        return Status.OK;
    }

    @Override
    public Status appendBatch(List<AvroFlumeEvent> events) throws AvroRemoteException {
        counterGroup.incrementAndGet("rpc.received.batch");

        List<Event> batch = new ArrayList<>();

        for (AvroFlumeEvent avroEvent : events) {
            Event event = EventBuilder.withBody(avroEvent.getBody().array(), toStringMap(avroEvent.getHeaders()));
            counterGroup.incrementAndGet("rpc.events");

            batch.add(event);
        }

        try {
            getChannelProcessor().processEventBatch(batch);
        } catch (Exception e) {
            logger.error("Unable to process event batch", e);
            return Status.FAILED;
        }

        return Status.OK;
    }

    private static Map<String, String> toStringMap(Map<CharSequence, CharSequence> charSeqMap) {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<CharSequence, CharSequence> entry : charSeqMap.entrySet()) {
            stringMap.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return stringMap;
    }
}