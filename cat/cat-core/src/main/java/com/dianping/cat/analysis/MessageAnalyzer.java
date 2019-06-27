package com.dianping.cat.analysis;

import com.dianping.cat.message.spi.MessageQueue;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.ReportManager;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public interface MessageAnalyzer {

    long getStartTime();

    boolean isEligable(MessageTree tree);

    void analyze(MessageQueue queue);

    void destroy();

    void doCheckpoint(boolean atEnd);

    ReportManager<?> getReportManager();

    void setIndex(int index);

    void initialize(long startTime, long duration, long extraTime);
}
