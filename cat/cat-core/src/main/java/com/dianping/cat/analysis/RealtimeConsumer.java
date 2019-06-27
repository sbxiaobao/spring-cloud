package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.statistic.ServerStatisticManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class RealtimeConsumer implements MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RealtimeConsumer.class);

    public static final long MINUTE = 60 * 1000L;
    public static final long HOUR = 60 * MINUTE;

    private static RealtimeConsumer INSTANCE = new RealtimeConsumer();
    private ServerStatisticManager serverStatisticManager = ServerStatisticManager.getInstance();
    private PeriodManager periodManager;

    public static RealtimeConsumer getInstance() {
        return INSTANCE;
    }

    private RealtimeConsumer() {
        periodManager = new PeriodManager(HOUR);
        Threads.forGroup("cat").start(periodManager);
    }

    @Override
    public void consume(MessageTree tree) {
        if (tree == null) {
            return;
        }
        long timestamp = tree.getMessage().getTimestamp();
        Period period = periodManager.findPeriod(timestamp);
        if (period != null) {
            period.distribute(tree);
        } else {
            serverStatisticManager.addNetworkTimeError(1);
        }
    }

    @Override
    public void doCheckpoint() {

    }
}
