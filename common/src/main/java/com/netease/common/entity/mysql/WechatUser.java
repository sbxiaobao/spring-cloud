package com.netease.common.entity.mysql;

import java.sql.Timestamp;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
public class WechatUser {

    private Long id;

    private String nickName;

    private String openid;

    private Long phone;

    private Long bindTime;

    private Timestamp dbUpdateTime;

    private Timestamp dbCreateTime;

    private Integer active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getBindTime() {
        return bindTime;
    }

    public void setBindTime(Long bindTime) {
        this.bindTime = bindTime;
    }

    public Timestamp getDbUpdateTime() {
        return dbUpdateTime;
    }

    public void setDbUpdateTime(Timestamp dbUpdateTime) {
        this.dbUpdateTime = dbUpdateTime;
    }

    public Timestamp getDbCreateTime() {
        return dbCreateTime;
    }

    public void setDbCreateTime(Timestamp dbCreateTime) {
        this.dbCreateTime = dbCreateTime;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
