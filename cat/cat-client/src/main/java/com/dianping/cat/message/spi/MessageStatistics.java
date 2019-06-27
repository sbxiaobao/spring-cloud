package com.dianping.cat.message.spi;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/9
 */
public interface MessageStatistics {

    Map<String, Long> getStatistics();

    void onBytes(int size);

    void onOverflowed(MessageTree tree);
}
