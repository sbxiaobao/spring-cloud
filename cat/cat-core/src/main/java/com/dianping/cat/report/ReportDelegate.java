package com.dianping.cat.report;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public interface ReportDelegate<T> {

    T makeReport(String domain, long startTime, long duration);
}