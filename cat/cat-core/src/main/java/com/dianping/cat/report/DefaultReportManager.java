package com.dianping.cat.report;

import com.dianping.cat.configuration.NetworkInterfaceManager;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dianping.cat.Constants.HOUR;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class DefaultReportManager<T> implements ReportManager<T> {

    private Map<Long, Map<String, T>> reports = new ConcurrentHashMap<>();

    private static final DefaultReportManager INSTANCE = new DefaultReportManager();
    private ReportDelegate<T> reportDelegate;

    public static DefaultReportManager getInstance() {
        return INSTANCE;
    }

    private DefaultReportManager() {
    }

    @Override
    public T getHourlyReport(long startTime, String domain, boolean createIfNotExist) {
        Map<String, T> tmps = reports.get(startTime);

        if (tmps == null && createIfNotExist) {
            synchronized (reports) {
                tmps = reports.get(startTime);

                if (tmps == null) {
                    tmps = new ConcurrentHashMap<>();
                    reports.put(startTime, tmps);
                }
            }
        }

        if (tmps == null) {
            tmps = new LinkedHashMap<>();
        }

        T tmp = tmps.get(domain);

        if (tmp == null && createIfNotExist) {
            synchronized (reports) {
                tmp = reportDelegate.makeReport(domain, startTime, HOUR);
                tmps.put(domain, tmp);
            }
        }
        if (tmp == null) {
            tmp = tmps.get(domain);
        }
        return tmp;
    }

    @Override
    public Map<String, T> getHourlyReports(long startTime) {
        Map<String, T> rets = reports.get(startTime);
        if (rets == null) {
            return Collections.emptyMap();
        }
        return rets;
    }

    private void storeDatabase(long startTime, Map<String, T> reports) {
        Date period = new Date(startTime);
        String ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();

        for (T report : reports.values()) {
//            String domain = reportDelegate
        }
    }

    @Override
    public void storeHourlyReports(long startTime, StoragePolicy policy, int index) {
        Map<String, T> tmps = reports.get(startTime);
        if (policy.forDatabase()) {
            storeDatabase(startTime, tmps);
        }
    }

    public static enum StoragePolicy {
        FILE,
        FILE_AND_DB;

        public boolean forDatabase() {
            return this == FILE_AND_DB;
        }

        public boolean forFile() {
            return this == FILE_AND_DB || this == FILE;
        }
    }
}
