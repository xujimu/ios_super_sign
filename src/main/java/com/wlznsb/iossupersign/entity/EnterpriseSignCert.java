package com.wlznsb.iossupersign.entity;

import java.util.Date;

/**
 * 企业证书
 */
public class EnterpriseSignCert {

    private Integer id;
    private String account;
    private String name;
    private String certPath;
    private String moblicPath;
    private String password;
    private String status;
    private Integer count;
    private String remark;
    private Date createTime;
    private Date expireTime;
    private String md5;

    public EnterpriseSignCert() {
    }

    public EnterpriseSignCert(Integer id, String account, String name, String certPath, String moblicPath, String password, String status, Integer count, String remark, Date createTime, Date expireTime, String md5) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.certPath = certPath;
        this.moblicPath = moblicPath;
        this.password = password;
        this.status = status;
        this.count = count;
        this.remark = remark;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "EnterpriseSignCert{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", certPath='" + certPath + '\'' +
                ", moblicPath='" + moblicPath + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", count=" + count +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", expireTime=" + expireTime +
                ", md5='" + md5 + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getMoblicPath() {
        return moblicPath;
    }

    public void setMoblicPath(String moblicPath) {
        this.moblicPath = moblicPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
