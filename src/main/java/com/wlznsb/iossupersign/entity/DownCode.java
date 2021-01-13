package com.wlznsb.iossupersign.entity;

import java.util.Date;

/**
 * 下载码
 */
public class DownCode {
    private Integer id;
    private String account;
    private String downCode;
    private Date createTime;
    private Date useTime;
    private Integer status;

    @Override
    public String toString() {
        return "DownCode{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", downCode='" + downCode + '\'' +
                ", createTime=" + createTime +
                ", useTime=" + useTime +
                ", status=" + status +
                '}';
    }

    public DownCode(Integer id, String account, String downCode, Date createTime, Date useTime, Integer status) {
        this.id = id;
        this.account = account;
        this.downCode = downCode;
        this.createTime = createTime;
        this.useTime = useTime;
        this.status = status;
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

    public String getDownCode() {
        return downCode;
    }

    public void setDownCode(String downCode) {
        this.downCode = downCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
