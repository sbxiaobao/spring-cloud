package com.dianping.cat.consumer.heartbeat;

import com.dianping.cat.analysis.AbstractMessageAnalyzer;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.heartbeat.model.entity.Machine;
import com.dianping.cat.consumer.heartbeat.model.entity.Period;
import com.dianping.cat.message.Heartbeat;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.DefaultReportManager;
import com.dianping.cat.report.ReportManager;
import com.dianping.cat.status.model.entity.*;
import com.dianping.cat.status.model.transform.DefaultSaxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class HeartbeatAnalyzer extends AbstractMessageAnalyzer<HeartbeatReport> {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatAnalyzer.class);

    private ReportManager<HeartbeatReport> reportManager = DefaultReportManager.getInstance();

    @Override
    protected void process(MessageTree tree) {
        String domain = tree.getDomain();

        HeartbeatReport report = reportManager.getHourlyReport(getStartTime(), domain, true);
        report.addIp(tree.getIpAddress());
        List<Heartbeat> heartbeats = tree.getHeartbeats();

        for (Heartbeat h : heartbeats) {
            if (h.getType().equalsIgnoreCase("hearttype")) {
                processHeartbeat(report, h, tree);
            }
        }
    }

    private Period buildHeartBeatInfo(Machine machine, Heartbeat heartbeat, long timestamp) {
        String xml = (String) heartbeat.getData();
        StatusInfo info;
        try {
            info = DefaultSaxParser.parse(xml);
            RuntimeInfo runtime = info.getRuntime();

            if (runtime != null) {
                machine.setClasspath(runtime.getJavaClasspath());
            } else {
                machine.setClasspath("");
            }

            translateHeartbeat(info);
        } catch (Exception e) {
            return null;
        }

        try {
            long current = timestamp / 1000 / 60;
            int minute = (int) (current % 60);
            Period period = new Period(minute);

            for (Map.Entry<String, Extension> entry : info.getExtensions().entrySet()) {
                String id = entry.getKey();
                Extension ext = entry.getValue();
                com.dianping.cat.consumer.heartbeat.model.entity.Extension extension = period.findOrCreateExtension(id);
                Map<String, ExtensionDetail> details = ext.getDetails();

                for (Map.Entry<String, ExtensionDetail> detail : details.entrySet()) {
                    ExtensionDetail extensionDetail = detail.getValue();

                    extension.findOrCreateDetail(extensionDetail.getId()).setValue(extensionDetail.getValue());
                }
            }
            return period;
        } catch (Exception e) {
            logger.error("Build heart beat info error", e);
            return null;
        }
    }

    private void processHeartbeat(HeartbeatReport report, Heartbeat heartbeat, MessageTree tree) {
        String ip = tree.getIpAddress();
        Machine machine = report.findOrCreateMachine(ip);
        Period period = buildHeartBeatInfo(machine, heartbeat, heartbeat.getTimestamp());

        if (period != null) {
            List<Period> periods = machine.getPeriods();

            if (periods.size() <= 60) {
                machine.getPeriods().add(period);
            }
        }
    }

    private void translateHeartbeat(StatusInfo info) {
        try {
            MessageInfo message = info.getMessage();

            if (message.getProduced() > 0 || message.getBytes() > 0) {
                Extension catExtension = info.findOrCreateExtension("CatUsage");

                catExtension.findOrCreateExtensionDetail("Produced").setValue(message.getProduced());
                catExtension.findOrCreateExtensionDetail("Overflowed").setValue(message.getOverflowed());
                catExtension.findOrCreateExtensionDetail("Bytes").setValue(message.getBytes());

                Extension system = info.findOrCreateExtension("System");
                OsInfo osInfo = info.getOs();

                system.findOrCreateExtensionDetail("LoadAverage").setValue(osInfo.getSystemLoadAverage());
                system.findOrCreateExtensionDetail("FreePhysicalMemory").setValue(osInfo.getFreePhysicalMemory());
                system.findOrCreateExtensionDetail("FreeSwapSpaceSize").setValue(osInfo.getFreeSwapSpace());

                Extension gc = info.findOrCreateExtension("GC");
                MemoryInfo memory = info.getMemory();
                List<GcInfo> gcs = memory.getGcs();

                if (gcs.size() >= 2) {
                    GcInfo newGc = gcs.get(0);
                    GcInfo oldGc = gcs.get(1);
                    gc.findOrCreateExtensionDetail("ParNewCount").setValue(newGc.getCount());
                    gc.findOrCreateExtensionDetail("ParNewTime").setValue(newGc.getTime());
                    gc.findOrCreateExtensionDetail("ConcurrentMarkSweepCount").setValue(oldGc.getCount());
                    gc.findOrCreateExtensionDetail("ConcurrentMarkSweepTime").setValue(oldGc.getTime());
                }

                Extension thread = info.findOrCreateExtension("FrameworkThread");
                ThreadsInfo threadInfo = info.getThread();

                thread.findOrCreateExtensionDetail("HttpThread").setValue(threadInfo.getHttpThreadCount());
                thread.findOrCreateExtensionDetail("CatThread").setValue(threadInfo.getCatThreadCount());
                thread.findOrCreateExtensionDetail("PigeonThread").setValue(threadInfo.getPigeonThreadCount());
                thread.findOrCreateExtensionDetail("ActiveThread").setValue(threadInfo.getCount());
                thread.findOrCreateExtensionDetail("StartedThread").setValue(threadInfo.getTotalStartedCount());

                Extension disk = info.findOrCreateExtension("Disk");
                List<DiskVolumeInfo> diskVolumes = info.getDisk().getDiskVolumes();

                for (DiskVolumeInfo vinfo : diskVolumes) {
                    disk.findOrCreateExtensionDetail(vinfo.getId() + " Free").setValue(vinfo.getFree());
                }
            }
        } catch (Exception ignored) {
            // support new java client
        }

        for (Extension ex : info.getExtensions().values()) {
            Map<String, String> propertis = ex.getDynamicAttributes();

            for (Map.Entry<String, String> entry : propertis.entrySet()) {
                try {
                    double value = Double.parseDouble(entry.getValue());

                    ex.findOrCreateExtensionDetail(entry.getKey()).setValue(value);
                } catch (Exception e) {
                    logger.error("StatusExtension can only be double type", e);
                }
            }
        }
    }

    @Override
    public ReportManager<HeartbeatReport> getReportManager() {
        return reportManager;
    }

    @Override
    public boolean isEligable(MessageTree tree) {
        return tree.getHeartbeats().size() > 0;
    }

    @Override
    public void doCheckpoint(boolean atEnd) {
        if (atEnd) {
//            reportManager.sto
        }
    }
}