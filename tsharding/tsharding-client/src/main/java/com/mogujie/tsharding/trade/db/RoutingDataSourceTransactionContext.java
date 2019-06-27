package com.mogujie.tsharding.trade.db;

import javax.sql.DataSource;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public class RoutingDataSourceTransactionContext {

    private static final ThreadLocal<DataSource> curDataSource = new ThreadLocal<>();

    public static DataSource getCurTransactionDataSource() {
        return curDataSource.get();
    }

    public static void setCurDataSource(DataSource dataSource) {
        curDataSource.set(dataSource);
    }

    public static void clear() {
        curDataSource.remove();
    }
}