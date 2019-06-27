package com.dianping.cat.consumer.heartbeat.model.entity;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Detail {

    private String id;

    private double value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Detail(String id) {
        this.id = id;
    }
}
