package com.mogujie.tsharding.trade.db;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public class ReadWriteSplittingContext {

    private static final ThreadLocal<DataSourceType> curDataSourceType = new ThreadLocal<>();

    public static void set(DataSourceType dataSourceType) {
        curDataSourceType.set(dataSourceType);
    }

    public static void setMaster() {
        curDataSourceType.set(DataSourceType.master);
    }

    public static void clear() {
        curDataSourceType.remove();
    }

    public static boolean isMaster() {
        return DataSourceType.master == curDataSourceType.get();
    }
}