package org.apache.flume.source;

import org.apache.flume.ChannelException;
import org.apache.flume.CounterGroup;
import org.apache.flume.PollableSource;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public class SequenceGeneratorSource extends AbstractSource implements PollableSource {

    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorSource.class);

    private long sequence;

    private CounterGroup counterGroup;

    public SequenceGeneratorSource() {
        sequence = 0;
        counterGroup = new CounterGroup();
    }

    @Override
    public Status process() {
        try {
            getChannelProcessor().processEvent(EventBuilder.withBody(String.valueOf(sequence++).getBytes()));
            counterGroup.incrementAndGet("events.successful");
        } catch (ChannelException e) {
            counterGroup.incrementAndGet("events.failed");
        }

        return Status.READY;
    }

    @Override
    public synchronized void start() {
        logger.info("Sequence generator source starting");

        super.start();

        logger.debug("Sequence generator source started");
    }

    @Override
    public synchronized void stop() {
        logger.info("Sequence generator source stopping");

        super.stop();

        logger.info("Sequence generator source stopped. Metrics:{}", counterGroup);
    }
}
