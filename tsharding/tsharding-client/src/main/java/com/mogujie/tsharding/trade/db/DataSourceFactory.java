package com.mogujie.tsharding.trade.db;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public interface DataSourceFactory<T extends DataSource> {

    T getDataSource(DataSourceConfig config) throws SQLException;
}