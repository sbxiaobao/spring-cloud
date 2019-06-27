package com.dianping.cat.status.jvm;

import com.dianping.cat.status.AbstraceCollector;
import com.dianping.cat.status.StatusExtensionRegister;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/1
 */
public class JvmInfoCollector {
    private static JvmInfoCollector collector = new JvmInfoCollector();
    private boolean hasOldGc = false;
    private long lastGcCount = 0;
    private long lastGcTime = 0;
    private long lastFullGcTime = 0;
    private long lastFullGcCount = 0;
    private long lastYoungGcTime = 0;
    private long lastYoungGcCount = 0;

    public static JvmInfoCollector getInstance() {
        return collector;
    }

    private Set<String> youngGcAlgorithm = new LinkedHashSet<String>() {
        {
            add("Copy");
            add("ParNew");
            add("PS Scavenge");
            add("G1 Young Generation");
        }
    };

    private Set<String> oldGcAlgorithm = new LinkedHashSet<String>() {
        {
            add("MarkSweepCompact");
            add("PS MarkSweep");
            add("ConcurrentMarkSweep");
            add("G1 Old Generation");
        }
    };

    public void registerJVMCollector() {
        final StatusExtensionRegister instance = StatusExtensionRegister.getInstance();

        instance.register(new AbstraceCollector() {
            @Override
            public String getId() {
                return "jvm.gc";
            }

            @Override
            public Map<String, String> getProperties() {
                Map<String, Number> map = collector.doGcCollect();
                return convert(map);
            }
        });

//        instance.register(new AbstraceCollector() {
//            @Override
//            public String getId() {
//                return "jvm.memory";
//            }
//
//            @Override
//            public Map<String, String> getProperties() {
//                Map<String, Number> map = collector.doMemoryCollect();
//                return convert(map);
//            }
//        });
    }

    private Map<String, Number> doGcCollect() {
        long gcCount = 0;
        long gcTime = 0;
        long oldGcCount = 0;
        long oldGcTime = 0;
        long youngGcCount = 0;
        long youngGcTime = 0;
        Map<String, Number> map = new LinkedHashMap<>();

        for (GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
            String gcAlgorithm = garbageCollector.getName();

            if (youngGcAlgorithm.contains(gcAlgorithm)) {
                youngGcTime += garbageCollector.getCollectionTime();
                youngGcCount += garbageCollector.getCollectionCount();
            } else if (oldGcAlgorithm.contains(gcAlgorithm)) {
                oldGcTime += garbageCollector.getCollectionTime();
                oldGcCount += garbageCollector.getCollectionCount();
            } else {
//                Cat.log("UnknownGcAlgorithm", gcAlgorithm);
            }
        }

        map.put("jvm.gc.count", gcCount - lastGcCount);
        map.put("jvm.gc.time", gcTime - lastGcTime);
        long value = oldGcCount - lastFullGcCount;

        if (value > 0) {
            hasOldGc = true;
        }

        map.put("jvm.fullgc.count", value);
        map.put("jvm.fullgc.time", oldGcTime - lastFullGcTime);
        map.put("jvm.younggc.count", youngGcCount - lastYoungGcCount);
        map.put("jvm.younggc.time", youngGcTime - lastYoungGcTime);

        if (youngGcCount > lastYoungGcCount) {
            map.put("jvm.younggc.meantime", (youngGcTime - lastYoungGcTime) / (youngGcCount - lastYoungGcCount));
        } else {
            map.put("jvm.younggc.meantime", 0);
        }

        lastGcCount = gcCount;
        lastGcTime = gcTime;
        lastYoungGcCount = youngGcCount;
        lastYoungGcTime = youngGcTime;

        return map;
    }

    private Map<String, Number> doMemoryCollect() {
        return null;
    }
}
