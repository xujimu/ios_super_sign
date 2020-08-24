package com.wlznsb.iossupersign.entity;

import java.util.Date;

/**
 * 打包状态
 */
public class PackStatus {
    private Integer id;
    private String account;
    private String pageName;
    private String uuid;
    private String udid;
    private String iis;
    private Date createTime;
    private String ipa;
    private String plist;
    private String status;
    //是否掉签
    private Integer signOff;


    public PackStatus() {
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

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getIis() {
        return iis;
    }

    public void setIis(String iis) {
        this.iis = iis;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getPlist() {
        return plist;
    }

    public void setPlist(String plist) {
        this.plist = plist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSignOff() {
        return signOff;
    }

    public void setSignOff(Integer signOff) {
        this.signOff = signOff;
    }

    public PackStatus(Integer id, String account, String pageName, String uuid, String udid, String iis, Date createTime, String ipa, String plist, String status, Integer signOff) {
        this.id = id;
        this.account = account;
        this.pageName = pageName;
        this.uuid = uuid;
        this.udid = udid;
        this.iis = iis;
        this.createTime = createTime;
        this.ipa = ipa;
        this.plist = plist;
        this.status = status;
        this.signOff = signOff;
    }

    @Override
    public String toString() {
        return "PackStatus{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", pageName='" + pageName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", udid='" + udid + '\'' +
                ", iis='" + iis + '\'' +
                ", createTime=" + createTime +
                ", ipa='" + ipa + '\'' +
                ", plist='" + plist + '\'' +
                ", status='" + status + '\'' +
                ", signOff=" + signOff +
                '}';
    }
}
