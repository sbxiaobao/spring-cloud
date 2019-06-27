package com.dianping.cat.statistic;


import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/22
 */
public class ServerStatisticManager {

    private static ServerStatisticManager INSTANCE = new ServerStatisticManager();
    public ServerStatistic serverStatistic = new ServerStatistic();
    private volatile ServerStatistic.Statistic currentStatistic = null;
    private volatile long currentMinute = -1;

    public static ServerStatisticManager getInstance() {
        return INSTANCE;
    }

    private ServerStatisticManager() {
    }

    public void addMessageTotalLoss(long total) {
        getCurrentStatistic().addMessageTotal(total);
    }

    public void addMessageTotalLoss(String domain, long total) {
        getCurrentStatistic().addMessageTotal(domain, total);
    }

    public void addMessageTotal(String domain, long total) {
        currentStatistic.addMessageTotal(domain, total);
    }

    public void addMessageTotal(long total) {
        getCurrentStatistic().addMessageTotal(total);
    }

    public void addNetworkTimeError(long total) {
        getCurrentStatistic().addNetworkTimeError(total);
    }

    private ServerStatistic.Statistic getCurrentStatistic() {
        long time = System.currentTimeMillis();

        time = time - time % (60 * 1000);

        if (time != currentMinute) {
            synchronized (this) {
                currentStatistic = serverStatistic.findOrCreate(time);
                currentMinute = time;
            }
        }
        return currentStatistic;
    }
}