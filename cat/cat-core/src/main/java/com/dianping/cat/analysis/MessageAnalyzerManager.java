package com.dianping.cat.analysis;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/24
 */
public interface MessageAnalyzerManager {

    List<String> getAnalyzerNames();

    List<MessageAnalyzer> getAnalyzer(String name, long startTime);
}
