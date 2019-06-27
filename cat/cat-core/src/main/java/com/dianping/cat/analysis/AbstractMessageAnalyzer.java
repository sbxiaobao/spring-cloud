package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageQueue;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.ReportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public abstract class AbstractMessageAnalyzer<R> implements MessageAnalyzer {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractMessageAnalyzer.class);

    protected long startTime;
    protected long duration;
    private long extraTime;

    protected int index;

    private long errors = 0;

    private AtomicBoolean active = new AtomicBoolean(true);

    @Override
    public void initialize(long startTime, long duration, long extraTime) {
        this.extraTime = extraTime;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public void analyze(MessageQueue queue) {
        while (!isTimeout() && isActive()) {
            MessageTree tree = queue.poll();

            if (tree != null) {
                try {
                    process(tree);
                } catch (Exception e) {
                    errors++;
                    logger.error("Analyze error, times:{}", errors, e);
                }
            }
        }
    }

    @Override
    public boolean isEligable(MessageTree tree) {
        return true;
    }

    @Override
    public void destroy() {
        ReportManager<?> manager = this.getReportManager();

        if (manager != null) {
//            manager
        }
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    protected boolean isTimeout() {
        long currentTime = System.currentTimeMillis();
        long endTime = startTime + duration + extraTime;

        return currentTime > endTime;
    }

    @Override
    public abstract void doCheckpoint(boolean atEnd);

    @Override
    public long getStartTime() {
        return startTime;
    }

    protected boolean isActive() {
        return active.get();
    }

    protected abstract void process(MessageTree tree);
}