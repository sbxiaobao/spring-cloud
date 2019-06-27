package com.dianping.cat.analysis;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class PeriodStrategy {

    private long duration;

    private long extraTime;

    private long aheadTime;

    private long lastStartTime;

    private long lastEndTime;

    public PeriodStrategy(long duration, long extraTime, long aheadTime) {
        this.duration = duration;
        this.extraTime = extraTime;
        this.aheadTime = aheadTime;
        lastStartTime = -1;
        lastEndTime = 0;
    }

    public long getDuration() {
        return duration;
    }

    public long next(long now) {
        long startTime = now - now % duration;

        if (startTime > lastStartTime) {
            lastStartTime = startTime;
            return startTime;
        }

        // prepare next period ahead
        if (now - lastStartTime >= duration - aheadTime) {
            lastStartTime = startTime + duration;
            return startTime + duration;
        }

        // last period is over
        if (now - lastEndTime >= duration + extraTime) {
            long lastEndTime = this.lastEndTime;
            lastEndTime = startTime;
            return -lastEndTime;
        }

        return 0;
    }

    public static void main(String[] args) {
        long extraTime = 3 * 60 * 1000L;
        long MINUTE = 60 * 1000L;
        Long HOUR = 60 * MINUTE;
        PeriodStrategy periodStrategy = new PeriodStrategy(HOUR, extraTime, extraTime);
        long next = periodStrategy.next(System.currentTimeMillis());
        System.out.println(next + " duration : " + periodStrategy.getDuration());
    }
}