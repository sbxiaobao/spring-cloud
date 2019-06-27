package com.dianping.cat.report;

import java.util.Map;

import static com.dianping.cat.report.DefaultReportManager.StoragePolicy;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public interface ReportManager<T> {

    T getHourlyReport(long startTime, String domain, boolean createIfNotExist);

    Map<String, T> getHourlyReports(long startTime);

    void storeHourlyReports(long startTime, StoragePolicy policy, int index);
}