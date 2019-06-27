package com.dianping.cat.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/22
 */
public class ServerStatistic {
    private Map<Long, Statistic> statistics = new ConcurrentHashMap<>(100);

    public synchronized Statistic findOrCreate(Long time) {
        Statistic state = statistics.get(time);

        if (state == null) {
            state = new Statistic();
            statistics.put(time, state);
        }
        return state;
    }

    public static class Statistic {
        private long messageTotal;
        private long messageTotalLoss;
        private long messageSize;
        private long messageDump;
        private long messageDumpLoss;
        private long networkTimeError;
        private ConcurrentHashMap<String, AtomicLong> messageTotals = new ConcurrentHashMap<>(256);
        private ConcurrentHashMap<String, AtomicLong> messageTotalLosses = new ConcurrentHashMap<>(256);

        public void addMessageTotal(long messageTotal) {
            messageTotal += messageTotal;
        }

        public void addMessageTotal(String domain, long messageTotal) {
            AtomicLong value = messageTotals.get(domain);

            if (value != null) {
                value.addAndGet(messageTotal);
            } else {
                messageTotals.put(domain, new AtomicLong(messageTotal));
            }
        }

        public void addMessageTotalLoss(long messageTotalLoss) {
            messageTotalLoss += messageTotalLoss;
        }

        public void addMessageTotalLoss(String domain, long messageTotalLoss) {
            AtomicLong value = messageTotalLosses.get(domain);

            if (value != null) {
                value.addAndGet(messageTotalLoss);
            } else {
                messageTotalLosses.put(domain, new AtomicLong(messageTotalLoss));
            }
        }

        public void addNetworkTimeError(long networkTimeError) {
            networkTimeError += networkTimeError;
        }
    }
}