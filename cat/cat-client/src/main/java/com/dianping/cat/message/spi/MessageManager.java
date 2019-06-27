package com.dianping.cat.message.spi;

import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/28
 */
public interface MessageManager {

    void setup();

    boolean hasContext();

    void add(Message message);

    MessageTree getThreadLocalMessageTree();

    void end(Transaction transaction);

    void reset();
}
