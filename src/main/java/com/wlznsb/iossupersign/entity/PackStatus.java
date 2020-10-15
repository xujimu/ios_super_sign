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
    //p12路径
    private String p12Path;
    //签名描述文件路径
    private String mobilePath;
    private Date createTime;

    @Override
    public String toString() {
        return "PackStatus{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", pageName='" + pageName + '\'' +
                ", uuid='" + uuid + '\'' +
                ", udid='" + udid + '\'' +
                ", iis='" + iis + '\'' +
                ", p12Path='" + p12Path + '\'' +
                ", mobilePath='" + mobilePath + '\'' +
                ", createTime=" + createTime +
                ", ipa='" + ipa + '\'' +
                ", plist='" + plist + '\'' +
                ", status='" + status + '\'' +
                ", signOff=" + signOff +
                ", appId=" + appId +
                ", url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                ", downCode='" + downCode + '\'' +
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

    public String getP12Path() {
        return p12Path;
    }

    public void setP12Path(String p12Path) {
        this.p12Path = p12Path;
    }

    public String getMobilePath() {
        return mobilePath;
    }

    public void setMobilePath(String mobilePath) {
        this.mobilePath = mobilePath;
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDownCode() {
        return downCode;
    }

    public void setDownCode(String downCode) {
        this.downCode = downCode;
    }

    public PackStatus(Integer id, String account, String pageName, String uuid, String udid, String iis, String p12Path, String mobilePath, Date createTime, String ipa, String plist, String status, Integer signOff, Integer appId, String url, String ip, String downCode) {
        this.id = id;
        this.account = account;
        this.pageName = pageName;
        this.uuid = uuid;
        this.udid = udid;
        this.iis = iis;
        this.p12Path = p12Path;
        this.mobilePath = mobilePath;
        this.createTime = createTime;
        this.ipa = ipa;
        this.plist = plist;
        this.status = status;
        this.signOff = signOff;
        this.appId = appId;
        this.url = url;
        this.ip = ip;
        this.downCode = downCode;
    }

    private String ipa;
    private String plist;
    private String status;
    //是否掉签
    private Integer signOff;
    private Integer appId;
    private String url;
    private String ip;
    private String downCode;


    public PackStatus() {
    }

}
