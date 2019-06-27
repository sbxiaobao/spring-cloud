package org.apache.flume.channel;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/26
 */
public enum DatabaseType {

    OTHER("OTHER", null),

    DERBY("DERBY", "values(1)"),

    MYSQL("MYSQL", "select 1"),

    POSTGRESQL("POSTGRESQL", null),

    ORACLE("ORACLE", null);

    private final String name;
    private final String validationQuery;

    private DatabaseType(String name, String validationQuery) {
        this.name = name;
        this.validationQuery = validationQuery;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public static DatabaseType getByName(String dbName) {
        DatabaseType type = null;
        try {
            type = DatabaseType.valueOf(dbName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            type = DatabaseType.OTHER;
        }
        return type;
    }
}