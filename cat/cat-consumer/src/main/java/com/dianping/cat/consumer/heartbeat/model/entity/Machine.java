package com.dianping.cat.consumer.heartbeat.model.entity;

import java.util.List;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Machine {
    private String ip;
    private List<Period> periods;
    private String classpath;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }
}