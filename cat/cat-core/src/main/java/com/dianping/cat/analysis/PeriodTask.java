package com.dianping.cat.analysis;

import com.dianping.cat.CatConstants;
import com.dianping.cat.message.spi.MessageQueue;
import com.dianping.cat.message.spi.MessageTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.helper.Threads;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class PeriodTask implements Threads.Task {

    private static final Logger logger = LoggerFactory.getLogger(PeriodTask.class);

    private long startTime;
    private MessageAnalyzer analyzer;
    private MessageQueue queue;
    private int queueOverflow;
    private int index;

    public PeriodTask(MessageAnalyzer analyzer, MessageQueue queue, long startTime) {
        this.analyzer = analyzer;
        this.queue = queue;
        this.startTime = startTime;
    }

    @Override
    public String getName() {
        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(startTime);
        return cal.get(Calendar.HOUR_OF_DAY) + "-" + index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void finish() {
    }

    public boolean enqueue(MessageTree tree) {
        if (analyzer.isEligable(tree)) {
            boolean result = queue.offer(tree);

            if (!result) {
                queueOverflow++;

                if (queueOverflow % (10 * CatConstants.ERROR_COUNT) == 0) {
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(analyzer.getStartTime());
                    logger.warn(analyzer.getClass().getSimpleName() + " queue overflow number " + queueOverflow + " analyzer time:" + date);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        try {
            analyzer.analyze(queue);
        } catch (Exception e) {
            logger.error("Analyzer error!", e);
        }
    }
}