package com.dianping.cat.consumer.heartbeat;

import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.report.ReportDelegate;

import java.util.Date;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class HeartbeatDelegate implements ReportDelegate<HeartbeatReport> {

    @Override
    public HeartbeatReport makeReport(String domain, long startTime, long duration) {
        HeartbeatReport report = new HeartbeatReport(domain);

        report.setStartTime(new Date(startTime));
        report.setEndTime(new Date(startTime + duration - 1));
        return report;
    }
}
