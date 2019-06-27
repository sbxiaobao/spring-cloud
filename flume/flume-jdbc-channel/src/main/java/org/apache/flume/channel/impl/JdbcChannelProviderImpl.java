package org.apache.flume.channel.impl;

import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.apache.flume.channel.DatabaseType;
import org.apache.flume.channel.JdbcChannelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public class JdbcChannelProviderImpl implements JdbcChannelProvider {

    private static final Logger logger = LoggerFactory.getLogger(JdbcChannelProviderImpl.class);

    private static final String EMBEDDED_DERBY_DRIVER_CLASSNAME = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final String DEFAULT_DRIVER_CLASSNAME = EMBEDDED_DERBY_DRIVER_CLASSNAME;

    private static final String DEFAULT_USERNAME = "sa";

    private static final String DEFAULT_PASSWORD = "";

    private static final String DEFAULT_DBTYPE = "DERBY";

    private GenericObjectPool connectionPool;

    private KeyedObjectPoolFactory statementPool;

    private DataSource dataSource;

    private DatabaseType databaseType;

    @Override
    public void initialize(Context context) {

    }

    @Override
    public void close() {

    }

    @Override
    public void persistEvent(String channelName, Event event) {

    }

    @Override
    public Event removeEvent(String channelName) {
        return null;
    }

    @Override
    public Transaction getTransaction() {
        return null;
    }
}
