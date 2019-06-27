package com.netease.repository.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
@EnableTransactionManagement
@Configuration
@EnableAspectJAutoProxy
public class MysqlConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MysqlConfiguration.class);

    @Bean
    public DataSource dataSource(@Value("${jdbc.driver}") String jdbcDriver,
                                 @Value("${jdbc.url}") String jdbcUrl,
                                 @Value("${jdbc.username}") String jdbcUsername,
                                 @Value("${jdbc.password}") String jdbcPassword,
                                 @Value("${dbcp2.initial.size:1}") Integer initialSize,
                                 @Value("${dbcp2.max.total:10}") Integer maxTotal,
                                 @Value("${dbcp2.max.idle:5}") Integer maxIdle,
                                 @Value("${dbcp2.min.idle:1}") Integer minIdle,
                                 @Value("${dbcp2.default.auto.commit}") Boolean defaultAutoCommit,
                                 @Value("${dbcp2.default.test.on.borrow:false}") Boolean testOnBorrow,
                                 @Value("${dbcp2.default.test.while.idle:true}") Boolean testWhileIdle,
                                 @Value("${dbcp2.default.test.on.return:false}") Boolean testOnReturn) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMinIdle(minIdle);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnReturn(testOnReturn);
        return dataSource;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.netease.repository.mysql.dao.core");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

    @Bean(name = "sqlSessionFactoryBean")
    public SqlSessionFactoryBean sessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sessionFactoryBean.setMapperLocations(resolver.getResources("classpath:config/mapper/*.xml"));
        } catch (IOException e) {
            logger.error("Load mybatis mapper error", e);
        }
        sessionFactoryBean.setTypeAliasesPackage("com.netease.common.entity.mysql");
        sessionFactoryBean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setUseGeneratedKeys(true);
        conf.setCacheEnabled(false);
        conf.setLogImpl(Slf4jImpl.class);
        conf.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
        conf.setMapUnderscoreToCamelCase(true);
        conf.setDefaultStatementTimeout(30);
        conf.setDefaultFetchSize(100);
        sessionFactoryBean.setConfiguration(conf);
        return sessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}