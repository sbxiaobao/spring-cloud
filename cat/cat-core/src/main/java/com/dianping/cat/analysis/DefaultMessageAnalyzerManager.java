package com.dianping.cat.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/24
 */
public class DefaultMessageAnalyzerManager implements MessageAnalyzerManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageAnalyzerManager.class);

    private static final long MINUTE = 60 * 1000L;
    private List<String> analyzerNames;
    private long extraTime = 3 * MINUTE;
    private long duration = 60 * MINUTE;

    private Map<Long, Map<String, List<MessageAnalyzer>>> analyzers = new HashMap<>();

    @Override
    public List<String> getAnalyzerNames() {
        return analyzerNames;
    }

    @Override
    public List<MessageAnalyzer> getAnalyzer(String name, long startTime) {
        analyzers.remove(startTime - duration * 2);

        Map<String, List<MessageAnalyzer>> map = analyzers.get(startTime);

        if (map == null) {
            synchronized (analyzers) {
                map = analyzers.get(startTime);

                if (map == null) {
                    map = new HashMap<>();
                    analyzers.put(startTime, map);
                }
            }
        }

        List<MessageAnalyzer> analyzerList = map.get(name);

        if (analyzerList == null) {
            synchronized (map) {
                analyzerList = map.get(name);

                if (analyzerList == null) {
                    analyzerList = new ArrayList<>();

                    MessageAnalyzer analyzer = lookup(MessageAnalyzer.class, name);

                    analyzer.setIndex(0);
                    analyzer.initialize(startTime, duration, extraTime);
                    analyzerList.add(analyzer);
                }
                map.put(name, analyzerList);
            }
        }
        return analyzerList;
    }

    public void initialize() {
        Map<String, MessageAnalyzer> map = lookupMap(MessageAnalyzer.class);

        analyzerNames = new ArrayList<>(map.keySet());

        Collections.sort(analyzerNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public <T> T lookup(Class<T> role, String name) {
        Map<String, T> map = new HashMap<>();
        for (String tmp : map.keySet()) {
            if (tmp.equalsIgnoreCase(name)) {
                return map.get(tmp);
            }
        }
        return null;
    }

    public <T> Map<String, T> lookupMap(Class<T> role) {
        Map<String, T> map = new HashMap<>();
        ServiceLoader<T> loader = ServiceLoader.load(role);
        Iterator<T> iter = loader.iterator();
        while (iter.hasNext()) {
            T t = iter.next();
            String name = t.getClass().getSimpleName();
            try {
                map.put(name, t);
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }

        }
        return map;
    }
}