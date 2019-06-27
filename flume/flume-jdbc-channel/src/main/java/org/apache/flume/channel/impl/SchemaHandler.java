package org.apache.flume.channel.impl;

import java.sql.Connection;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public interface SchemaHandler {

    boolean schemaExists();

    void validateSchema();

    void createSchemaObjects(boolean createIndex);

    void storeEvent(PersistableEvent event);

    PersistableEvent fetchAndDeleteEvent(String channel, Connection connection);

    long getChannelSize(Connection connection);
}