package com.mogujie.tsharding.trade.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public class DataSourceScanner implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final String PROPERTIES_FILE_NAME = "jdbc.properties";

    private static final String KEY_SEPARATOR = ".";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSourceFactory<? extends DataSource> dataSourceFactory;

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        final Map<String, ReadWriteSplittingDataSource> dataSources = new HashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (in != null) {
            Properties properties = new Properties();
            try {
                properties.load(in);
            } catch (IOException e) {
                throw new BeanInitializationException("Read property file error!", e);
            }
            try {
                Map<String, Map<DataSourceType, DataSource>> dataSourceMapping = this.getDataSources(properties);
                this.registerDataSource(registry, dataSourceMapping);
                int transactionManagerCount = 0;
                String transactionManagerBeanName = null;
                for (Map.Entry<String, Map<DataSourceType, DataSource>> entry : dataSourceMapping.entrySet()) {
                    final String name = entry.getKey();

                    final DataSource masterDataSource = entry.getValue().get(DataSourceType.master);

                    final ReadWriteSplittingDataSource readWriteSplittingDataSource = new ReadWriteSplittingDataSource(entry.getKey(), entry.getValue().get(DataSourceType.master), entry.getValue().get(DataSourceType.slave));
                    logger.info("Init dataSource {}", readWriteSplittingDataSource);
                    dataSources.put(name, readWriteSplittingDataSource);

                    if (masterDataSource == null) {
                        continue;
                    }

                    transactionManagerBeanName = name + "TransactionManager";

                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(RoutingDataSourceTransactionManager.class);

                    MutablePropertyValues propertyValues = new MutablePropertyValues();
                    propertyValues.add("dataSource", readWriteSplittingDataSource);
                    propertyValues.add("name", name);
                    beanDefinition.setPropertyValues(propertyValues);
                    AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(Qualifier.class);
                    qualifier.setAttribute(AutowireCandidateQualifier.VALUE_KEY, name);
                    beanDefinition.addQualifier(qualifier);
                    registry.registerBeanDefinition(transactionManagerBeanName, beanDefinition);

                    PlatformTransactionManager transactionManager = this.applicationContext.getBean(transactionManagerBeanName, PlatformTransactionManager.class);
                    Assert.notNull(transactionManager, "Register BeanDefinition of " + transactionManagerBeanName + " error!");
                    transactionManagerCount++;
                }

                if (transactionManagerCount == 1) {
                    registry.registerAlias(transactionManagerBeanName, "transactionManager");
                } else if (transactionManagerCount == 0) {
                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(EmptyTransactionManager.class);

                    registry.registerBeanDefinition("transactionManager", beanDefinition);
                }
            } catch (SQLException e) {
                throw new BeanCreationException("Initial dataSources error!", e);
            }
        }

        GenericBeanDefinition dataSourceLookupBeanDefinition = new GenericBeanDefinition();
        dataSourceLookupBeanDefinition.setBeanClass(DataSourceLookup.class);
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, dataSources);
        dataSourceLookupBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
        registry.registerBeanDefinition("dataSourceLookup", dataSourceLookupBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // do nothing
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, Map<DataSourceType, DataSource>> getDataSources(Properties properties) throws SQLException {
        Map<String, Map<DataSourceType, DataSource>> dataSourcesMapping = new HashMap<>(2);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String[] parts = entry.getKey().toString().trim().split("\\" + KEY_SEPARATOR);
            if (parts.length == 3) {
                String name = parts[0];
                if (!dataSourcesMapping.containsKey(name)) {
                    for (DataSourceType dataSourceType : DataSourceType.values()) {
                        DataSource ds = this.dataSourceFactory.getDataSource(this.parseDataSourceConfig(name, dataSourceType, properties));
                        Map<DataSourceType, DataSource> map = dataSourcesMapping.get(name);
                        if (map == null) {
                            map = new EnumMap<>(DataSourceType.class);
                            dataSourcesMapping.put(name, map);
                        }
                        DataSource preValue = map.put(dataSourceType, ds);
                        if (preValue != null) {
                            throw new IllegalArgumentException("Dupilicated DataSource of" + name + " " + dataSourceType);
                        }
                    }
                }
            } else {
            }
        }
        return dataSourcesMapping;
    }

    public void setDataSourceFactory(DataSourceFactory<? extends DataSource> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    private DataSourceConfig parseDataSourceConfig(String name, DataSourceType dataSourceType, Properties properties) {
        String keyPrefix = name + KEY_SEPARATOR + dataSourceType + KEY_SEPARATOR;

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        String url = properties.getProperty(keyPrefix + "url");
        Assert.hasText(url, keyPrefix + "url is empty!");
        dataSourceConfig.setUrl(url);

        String username = properties.getProperty(keyPrefix + "username");
        Assert.hasText(username, keyPrefix + "username is empty!");
        dataSourceConfig.setUsername(username);

        String password = properties.getProperty(keyPrefix + "password");
        Assert.hasText(password, keyPrefix + "password is empty!");
        dataSourceConfig.setPassword(password);

        String initialPoolSizeStr = properties.getProperty(keyPrefix + "initialPoolSize");
        int initialPoolSize = initialPoolSizeStr == null ? DataSourceConfig.DEFAULT_INI_POOL_SIZE : Integer.parseInt(initialPoolSizeStr);
        dataSourceConfig.setInitialPoolSize(initialPoolSize);

        String minPoolSizeStr = properties.getProperty(keyPrefix + "minPoolSize");
        int minPoolSize = minPoolSizeStr == null ? DataSourceConfig.DEFAULT_MIN_POOL_SIZE : Integer
                .parseInt(minPoolSizeStr);
        dataSourceConfig.setMinPoolSize(minPoolSize);

        String maxPoolSizeStr = properties.getProperty(keyPrefix + "maxPoolSize");
        int maxPoolSize = maxPoolSizeStr == null ? DataSourceConfig.DEFAULT_MAX_POOL_SIZE : Integer
                .parseInt(maxPoolSizeStr);
        dataSourceConfig.setMaxPoolSize(maxPoolSize);

        return dataSourceConfig;
    }

    private void registerDataSource(BeanDefinitionRegistry registry, Map<String, Map<DataSourceType, DataSource>> dataSourcesMapping) {
        for (Map.Entry<String, Map<DataSourceType, DataSource>> entry : dataSourcesMapping.entrySet()) {
            final String name = entry.getKey();
            for (Map.Entry<DataSourceType, DataSource> subEntry : entry.getValue().entrySet()) {
                GenericBeanDefinition dataSourceBeanDefinition = new GenericBeanDefinition();
                dataSourceBeanDefinition.setBeanClass(DataSourceFactoryBean.class);
                ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addIndexedArgumentValue(0, subEntry.getValue());
                dataSourceBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
                String beanName = name + Character.toUpperCase(subEntry.getKey().name().charAt(0)) + subEntry.getKey().name().substring(1) + "DataSource";
                registry.registerBeanDefinition(beanName, dataSourceBeanDefinition);
            }
        }
    }

    public static class EmptyTransactionManager implements PlatformTransactionManager {
        @Override
        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void commit(TransactionStatus status) throws TransactionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void rollback(TransactionStatus status) throws TransactionException {
            throw new UnsupportedOperationException();
        }
    }

    public static class DataSourceFactoryBean implements FactoryBean<DataSource> {

        private final DataSource dataSource;

        public DataSourceFactoryBean(DataSource dataSource) {
            Assert.notNull(dataSource);
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getObject() throws Exception {
            return dataSource;
        }

        @Override
        public Class<?> getObjectType() {
            return this.dataSource.getClass();
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }
}