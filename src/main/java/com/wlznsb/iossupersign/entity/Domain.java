package com.wlznsb.iossupersign.entity;

import java.util.Date;


/**
 * 多域名
 */
public class Domain {
    private Integer id;
    private String domain;
    private Date createTime;
    private Integer status;

    public Domain(Integer id, String domain, Date createTime, Integer status) {
        this.id = id;
        this.domain = domain;
        this.createTime = createTime;
        this.status = status;
    }

    public Domain() {
    }

    @Override
    public String toString() {
        return "Domain{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
