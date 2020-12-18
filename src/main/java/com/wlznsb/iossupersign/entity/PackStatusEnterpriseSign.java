package com.wlznsb.iossupersign.entity;

import java.util.Date;

/**
 * 企业签名打包状态
 */
public class PackStatusEnterpriseSign {

    private Integer id;
    private Integer certId;
    private String certName;
    private String account;
    private Date createTime;
    private String appName;
    private String pageName;
    private String version;
    private String status;
    private String downUrl;
    private String ipaPath;
    private String url;

    public PackStatusEnterpriseSign() {
    }

    public PackStatusEnterpriseSign(Integer id, Integer certId, String certName, String account, Date createTime, String appName, String pageName, String version, String status, String downUrl, String ipaPath, String url) {
        this.id = id;
        this.certId = certId;
        this.certName = certName;
        this.account = account;
        this.createTime = createTime;
        this.appName = appName;
        this.pageName = pageName;
        this.version = version;
        this.status = status;
        this.downUrl = downUrl;
        this.ipaPath = ipaPath;
        this.url = url;
    }

    @Override
    public String toString() {
        return "PackStatusEnterpriseSign{" +
                "id=" + id +
                ", certId=" + certId +
                ", certName='" + certName + '\'' +
                ", account='" + account + '\'' +
                ", createTime=" + createTime +
                ", appName='" + appName + '\'' +
                ", pageName='" + pageName + '\'' +
                ", version='" + version + '\'' +
                ", status='" + status + '\'' +
                ", downUrl='" + downUrl + '\'' +
                ", ipaPath='" + ipaPath + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getIpaPath() {
        return ipaPath;
    }

    public void setIpaPath(String ipaPath) {
        this.ipaPath = ipaPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
