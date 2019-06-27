package org.apache.flume;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/19
 */
public interface Transaction {

    enum TransactionState {
        Started, Commited, RolledBack, Closed
    }

    void begin();

    void commit();

    void rollback();

    void close();
}
