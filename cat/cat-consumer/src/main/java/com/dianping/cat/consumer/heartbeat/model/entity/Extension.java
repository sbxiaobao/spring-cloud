package com.dianping.cat.consumer.heartbeat.model.entity;

import java.util.Map;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/4/16
 */
public class Extension {

    private String id;

    private Map<String, Detail> details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Detail> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Detail> details) {
        this.details = details;
    }

    public Extension(String id) {
        this.id = id;
    }

    public Detail findOrCreateDetail(String id) {
        Detail detail = details.get(id);

        if (detail == null) {
            synchronized (details) {
                detail = details.get(id);
                if (detail == null) {
                    detail = new Detail(id);
                    details.put(id, detail);
                }
            }
        }
        return detail;
    }
}