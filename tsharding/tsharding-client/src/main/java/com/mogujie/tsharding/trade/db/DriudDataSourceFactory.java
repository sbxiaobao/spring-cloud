package com.mogujie.tsharding.trade.db;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;

import java.util.Collections;
import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public class DriudDataSourceFactory implements DataSourceFactory<DruidDataSource> {

    private List<Filter> filters = Collections.emptyList();

    @Override
    public DruidDataSource getDataSource(DataSourceConfig config) throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());

        dataSource.setInitialSize(config.getInitialPoolSize());
        dataSource.setMinIdle(config.getMinPoolSize());
        dataSource.setMaxActive(config.getMaxPoolSize());

        dataSource.setFilters("stat");
        dataSource.setMaxWait(1000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(120000);
        dataSource.setTimeBetweenLogStatsMillis(0);

        dataSource.setProxyFilters(filters);

        dataSource.init();
        return dataSource;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
