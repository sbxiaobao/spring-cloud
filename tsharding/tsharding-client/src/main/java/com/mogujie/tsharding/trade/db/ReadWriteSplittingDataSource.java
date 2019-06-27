package com.mogujie.tsharding.trade.db;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/25
 */
public class ReadWriteSplittingDataSource extends AbstractDataSource implements DataSource, Closeable {

    private String name;
    private DataSource masterDataSource;
    private DataSource slaveDataSource;

    public ReadWriteSplittingDataSource(String name, DataSource masterDataSource, DataSource slaveDataSource) {
        this.name = name;
        this.masterDataSource = masterDataSource;
        this.slaveDataSource = slaveDataSource;

        Assert.isTrue(masterDataSource != slaveDataSource || masterDataSource != null, "masterDataSource and slaveDataSource cant't be both null!");
    }

    @Override
    public void close() throws IOException {
        if (this.masterDataSource != null) {
            if (masterDataSource instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) masterDataSource).close();
                } catch (Exception ignore) {
                }
            }
        }

        if (this.slaveDataSource != null) {
            if (slaveDataSource instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) slaveDataSource).close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    private DataSource determineTargetDataSource() {
        if (slaveDataSource == null) {
            return masterDataSource;
        }

        if (this.isInTransaction()) {
            return masterDataSource;
        }

        return ReadWriteSplittingContext.isMaster() ? masterDataSource : slaveDataSource;
    }

    private boolean isInTransaction() {
        return RoutingDataSourceTransactionContext.getCurTransactionDataSource() != null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReadWriteSplittingDataSource [");
        if (name != null) {
            builder.append("name=").append(name).append(", ");
        }
        if (masterDataSource != null) {
            builder.append("masterDataSource=").append(masterDataSource).append(", ");
        }
        if (slaveDataSource != null) {
            builder.append("slaveDataSource=").append(slaveDataSource);
        }
        builder.append("]");
        return builder.toString();
    }
}
