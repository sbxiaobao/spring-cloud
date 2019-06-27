package com.kafka.cluster;

import com.kafka.common.protocol.SecurityProtocol;
import com.kafka.common.utils.Utils;

import java.nio.ByteBuffer;

import static com.kafka.api.ApiUtils.*;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/29
 */
public class EndPoint {

    private String uriParseExp = "^(.*)://[?([0-9a-zA-Z-%.:]*)]?:(-?[0-9]+)";

    private String host;
    private Integer port;
    private SecurityProtocol protocolType;

    public EndPoint(String host, Integer port, SecurityProtocol protocolType) {
        this.host = host;
        this.port = port;
    }

    public void writeTo(ByteBuffer buffer) {
        buffer.putInt(port);
        writeShortString(buffer, host);
        buffer.putShort(protocolType.id);
    }

    public int sizeInBytes() {
        return 4 + shortStringLength(host) + 2;
    }

    public String connectionString() {
        String hostport = host == null ? ":" + port : Utils.formatAddress(host, port);
        return protocolType + "://" + hostport;
    }

    public static EndPoint creatEndPoint(String connectionString) {

        return null;
    }

    public static EndPoint readFrom(ByteBuffer buffer) {
        int port = buffer.getInt();
        String host = readShortString(buffer);
        short protocol = buffer.getShort();
        return new EndPoint(host, port, SecurityProtocol.forId(protocol));
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}