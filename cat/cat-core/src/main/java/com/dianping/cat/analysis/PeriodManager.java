package com.dianping.cat.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class PeriodManager implements Threads.Task {
    private static final Logger logger = LoggerFactory.getLogger(PeriodManager.class);
    public static long EXTRATIME = 3 * 60 * 1000L;

    private List<Period> periods = new ArrayList<>();
    private boolean active;

    private PeriodStrategy strategy;

    public PeriodManager(long duration) {
        strategy = new PeriodStrategy(duration, EXTRATIME, EXTRATIME);
        this.active = true;

        init();
    }

    @Override
    public String getName() {
        return "RealtimeConsumer-PeriodManager";
    }

    @Override
    public void shutdown() {
        active = false;
    }

    public void init() {
        long startTime = strategy.next(System.currentTimeMillis());
        startPeriod(startTime);
    }

    private void startPeriod(long startTime) {
        long endTime = startTime + strategy.getDuration();
        Period period = new Period(startTime, endTime);

        periods.add(period);
    }

    public Period findPeriod(long timestamp) {
        for (Period period : periods) {
            if (period.isIn(timestamp)) {
                return period;
            }
        }
        return null;
    }

    private void endPeriod(long startTime) {
        int len = periods.size();

        for (int i = 0; i < len; i++) {
            Period period = periods.get(i);

            if (period.isIn(startTime)) {
                period.finish();
                periods.remove(i);
                break;
            }
        }
    }

    @Override
    public void run() {
        while (active) {
            try {
                long now = System.currentTimeMillis();
                long value = strategy.next(now);

                if (value > 0) {
                    startPeriod(value);
                } else if (value < 0) {
                    Threads.forGroup("cat").start(new EndTaskThread(-value));
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private class EndTaskThread implements Threads.Task {
        private long startTime;

        public EndTaskThread(long startTime) {
            this.startTime = startTime;
        }

        @Override
        public String getName() {
            return "End-Consumer-Task";
        }

        @Override
        public void run() {
            endPeriod(startTime);
        }

        @Override
        public void shutdown() {

        }
    }
}
