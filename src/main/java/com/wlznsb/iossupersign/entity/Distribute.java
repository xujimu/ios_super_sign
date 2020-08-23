package com.wlznsb.iossupersign.entity;


import java.lang.ref.PhantomReference;
import java.util.Date;

/**
 * 分发应用信息和位置
 */
public class Distribute {

    private Integer id;
    private String account;
    private String name;
    private String version;
    private String icon;
    private String ipa;
    private String apk;
    private Date createTime;

    public Distribute() {
    }

    @Override
    public String toString() {
        return "Distribute{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", icon='" + icon + '\'' +
                ", ipa='" + ipa + '\'' +
                ", apk='" + apk + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Distribute(Integer id, String account, String name, String version, String icon, String ipa, String apk, Date createTime) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.version = version;
        this.icon = icon;
        this.ipa = ipa;
        this.apk = apk;
        this.createTime = createTime;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
