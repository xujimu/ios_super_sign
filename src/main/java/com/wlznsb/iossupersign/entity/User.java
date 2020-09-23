package com.wlznsb.iossupersign.entity;

import javax.annotation.Resource;
import java.util.Date;

public class User {

    private Integer id;
    private String account;
    private String password;
    private Date createTime;
    private Integer type;
    private Integer count;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                ", count=" + count +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public User(Integer id, String account, String password, Date createTime, Integer type, Integer count) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.createTime = createTime;
        this.type = type;
        this.count = count;
    }
}
