package org.apache.flume.source;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/6/18
 */
public enum SourceType {
    OTHER(null),

    NETCAT(NetcatSource.class.getName()),

    EXEC(ExecSource.class.getName()),

    AVRO(AvroSource.class.getName());

    private final String sourceClassName;

    private SourceType(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }
}
