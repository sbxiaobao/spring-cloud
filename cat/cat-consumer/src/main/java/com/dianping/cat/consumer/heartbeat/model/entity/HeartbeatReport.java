package com.dianping.cat.consumer.heartbeat.model.entity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class HeartbeatReport {
    private String domain;
    private Date startTime;
    private Date endTime;
    private Set<String> domainNames;
    private Set<String> ips = new HashSet<>();
    private Map<String, Machine> machines = new ConcurrentHashMap<>();

    public HeartbeatReport(String domain) {
        this.domain = domain;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void addIp(String ip) {
        ips.add(ip);
    }

    public Machine findOrCreateMachine(String ip) {
        Machine machine = machines.get(ip);
        if (machine == null) {
            synchronized (machines) {
                machine = machines.get(ip);

                if (machine == null) {
                    machine = new Machine();
                    machine.setIp(ip);

                    machines.put(ip, machine);
                }
            }
        }
        machine = machines.get(ip);
        return machine;
    }
}