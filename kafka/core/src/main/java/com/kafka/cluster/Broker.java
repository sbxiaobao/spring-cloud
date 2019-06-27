package com.kafka.cluster;

import com.alibaba.fastjson.JSONObject;
import com.kafka.common.BrokerNotAvailableException;
import com.kafka.common.KafkaException;
import com.kafka.common.protocol.SecurityProtocol;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/5/5
 */
public class Broker {

    private int id;
    private Map<SecurityProtocol, EndPoint> endPoints;
    private String rack;

    public Broker(int id, Map<SecurityProtocol, EndPoint> endPoints, String rack) {
        this.id = id;
        this.endPoints = endPoints;
        this.rack = rack;
    }

    public Broker(int id, Map<SecurityProtocol, EndPoint> endPoints) {
        this(id, endPoints, null);
    }

    public static Broker createBroker(int id, String brokerInfoString) {
        if (brokerInfoString == null) {
            throw new BrokerNotAvailableException(String.format("Broker id %s does not exist", id));
        }
        Map<String, Object> brokerInfo = JSONObject.parseObject(brokerInfoString, Map.class);
        if (brokerInfo == null) {
            throw new BrokerNotAvailableException(String.format("Broker id %s does not exist", id));
        }
        int version = (Integer) brokerInfo.get("version");
        if (version < 1) {
            throw new KafkaException(String.format("Unsupported version of broker registration:", brokerInfoString));
        } else if (version == 1) {
            String host = (String) brokerInfo.get("host");
            int port = (Integer) brokerInfo.get("port");
        } else {
            List<String> listeners = (List<String>) brokerInfo.get("endpoints");
            for (String listener : listeners) {
                EndPoint.c
            }
        }
        return null;
    }

}