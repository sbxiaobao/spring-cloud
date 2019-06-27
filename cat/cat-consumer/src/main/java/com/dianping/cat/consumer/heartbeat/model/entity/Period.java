package com.dianping.cat.consumer.heartbeat.model.entity;

import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Period {
    private int minute;
    private int threadCount;
    private int totalStartedCount;
    private int catThreadCount;
    private int pigeonThreadCount;
    private int httpThreadCount;
    private long newGcCount;
    private long oldGcCount;
    private long memoryFree;
    private long heapUsage;
    private long noneHeapUsage;
    private long systemLoadAverage;
    private long catMessageProduced;
    private long catMessageOverflow;
    private double catMessageSize;
    private List<Disk> disks;
    private Map<String, Extension> extensions;

    public Period(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getTotalStartedCount() {
        return totalStartedCount;
    }

    public void setTotalStartedCount(int totalStartedCount) {
        this.totalStartedCount = totalStartedCount;
    }

    public int getCatThreadCount() {
        return catThreadCount;
    }

    public void setCatThreadCount(int catThreadCount) {
        this.catThreadCount = catThreadCount;
    }

    public int getPigeonThreadCount() {
        return pigeonThreadCount;
    }

    public void setPigeonThreadCount(int pigeonThreadCount) {
        this.pigeonThreadCount = pigeonThreadCount;
    }

    public int getHttpThreadCount() {
        return httpThreadCount;
    }

    public void setHttpThreadCount(int httpThreadCount) {
        this.httpThreadCount = httpThreadCount;
    }

    public long getNewGcCount() {
        return newGcCount;
    }

    public void setNewGcCount(long newGcCount) {
        this.newGcCount = newGcCount;
    }

    public long getOldGcCount() {
        return oldGcCount;
    }

    public void setOldGcCount(long oldGcCount) {
        this.oldGcCount = oldGcCount;
    }

    public long getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(long memoryFree) {
        this.memoryFree = memoryFree;
    }

    public long getHeapUsage() {
        return heapUsage;
    }

    public void setHeapUsage(long heapUsage) {
        this.heapUsage = heapUsage;
    }

    public long getNoneHeapUsage() {
        return noneHeapUsage;
    }

    public void setNoneHeapUsage(long noneHeapUsage) {
        this.noneHeapUsage = noneHeapUsage;
    }

    public long getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(long systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public long getCatMessageProduced() {
        return catMessageProduced;
    }

    public void setCatMessageProduced(long catMessageProduced) {
        this.catMessageProduced = catMessageProduced;
    }

    public long getCatMessageOverflow() {
        return catMessageOverflow;
    }

    public void setCatMessageOverflow(long catMessageOverflow) {
        this.catMessageOverflow = catMessageOverflow;
    }

    public double getCatMessageSize() {
        return catMessageSize;
    }

    public void setCatMessageSize(double catMessageSize) {
        this.catMessageSize = catMessageSize;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public Map<String, Extension> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Extension> extensions) {
        this.extensions = extensions;
    }

    public Extension findOrCreateExtension(String id) {
        Extension extension = extensions.get(id);
        if (extension == null) {
            synchronized (extensions) {
                extension = extensions.get(id);
                if (extension == null) {
                    extension = new Extension(id);

                    extensions.put(id, extension);
                }
            }
        }
        return extension;
    }
}