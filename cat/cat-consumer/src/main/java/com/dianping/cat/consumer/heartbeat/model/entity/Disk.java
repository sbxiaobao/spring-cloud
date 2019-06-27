package com.dianping.cat.consumer.heartbeat.model.entity;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Disk {

    private String path;
    private long total;
    private long free;
    private long usable;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getUsable() {
        return usable;
    }

    public void setUsable(long usable) {
        this.usable = usable;
    }
}