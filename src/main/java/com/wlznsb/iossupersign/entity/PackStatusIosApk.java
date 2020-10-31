package com.wlznsb.iossupersign.entity;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class PackStatusIosApk {
    //id
    private Integer id;
    //用户账号
    @NotEmpty(message = "账号不能为空")
    private String account;
    //创建时间
    private Date createTime;
    //软件名称
    @NotEmpty(message = "软件不能为空")
    private String appName;
    //打包网址
    @NotEmpty(message = "打包网址不能为空")
    private String url;
    //名称
    private String name;
    //机构
    private String organization;
    //描述
    private String describe;
    //同意信息
    private String consentMessage;
    //应用图标
    @NotEmpty(message = "应用图标不能为空")
    private String icon;
    //启动图
    @NotEmpty(message = "启动图不能为空")
    private String startIcon;
    //是否可移除 0否 1是 xml文件false就是不能移除
    @NotEmpty(message = "是否可移除不能为空")
    private Integer isRemove;
    //是否是动态网址
    private Integer isVariable;
    //包名
    @NotEmpty(message = "包名不能为空")
    private String pageName;
    //版本
    @NotEmpty(message = "版本不能为空")
    private String version;
    //是否集成x5 0否 1是
    @NotEmpty(message = "是否集成x5不能为空")
    private Integer isXfive;
    //打包状态
    private String status;
    //预览地址
    private String preview;
    //源码下载地址
    private String down;
    //源码过期时间
    private Date expirationTime;
    //根证书
    private String rootCert;

    //服务器证书
    private String serverCert;
    //服务器秘钥
    private String keyCert;

    //备注
    private String remark;

    public PackStatusIosApk() {
    }

    @Override
    public String toString() {
        return "PackStatusIosApk{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", createTime=" + createTime +
                ", appName='" + appName + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", organization='" + organization + '\'' +
                ", describe='" + describe + '\'' +
                ", consentMessage='" + consentMessage + '\'' +
                ", icon='" + icon + '\'' +
                ", startIcon='" + startIcon + '\'' +
                ", isRemove=" + isRemove +
                ", isVariable=" + isVariable +
                ", pageName='" + pageName + '\'' +
                ", version='" + version + '\'' +
                ", isXfive=" + isXfive +
                ", status='" + status + '\'' +
                ", preview='" + preview + '\'' +
                ", down='" + down + '\'' +
                ", expirationTime=" + expirationTime +
                ", rootCert='" + rootCert + '\'' +
                ", serverCert='" + serverCert + '\'' +
                ", keyCert='" + keyCert + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public PackStatusIosApk(Integer id, @NotEmpty(message = "账号不能为空") String account, Date createTime, @NotEmpty(message = "软件不能为空") String appName, @NotEmpty(message = "打包网址不能为空") String url, String name, String organization, String describe, String consentMessage, @NotEmpty(message = "应用图标不能为空") String icon, @NotEmpty(message = "启动图不能为空") String startIcon, @NotEmpty(message = "是否可移除不能为空") Integer isRemove, Integer isVariable, @NotEmpty(message = "包名不能为空") String pageName, @NotEmpty(message = "版本不能为空") String version, @NotEmpty(message = "是否集成x5不能为空") Integer isXfive, String status, String preview, String down, Date expirationTime, String rootCert, String serverCert, String keyCert, String remark) {
        this.id = id;
        this.account = account;
        this.createTime = createTime;
        this.appName = appName;
        this.url = url;
        this.name = name;
        this.organization = organization;
        this.describe = describe;
        this.consentMessage = consentMessage;
        this.icon = icon;
        this.startIcon = startIcon;
        this.isRemove = isRemove;
        this.isVariable = isVariable;
        this.pageName = pageName;
        this.version = version;
        this.isXfive = isXfive;
        this.status = status;
        this.preview = preview;
        this.down = down;
        this.expirationTime = expirationTime;
        this.rootCert = rootCert;
        this.serverCert = serverCert;
        this.keyCert = keyCert;
        this.remark = remark;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getConsentMessage() {
        return consentMessage;
    }

    public void setConsentMessage(String consentMessage) {
        this.consentMessage = consentMessage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStartIcon() {
        return startIcon;
    }

    public void setStartIcon(String startIcon) {
        this.startIcon = startIcon;
    }

    public Integer getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Integer isRemove) {
        this.isRemove = isRemove;
    }

    public Integer getIsVariable() {
        return isVariable;
    }

    public void setIsVariable(Integer isVariable) {
        this.isVariable = isVariable;
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

    public Integer getIsXfive() {
        return isXfive;
    }

    public void setIsXfive(Integer isXfive) {
        this.isXfive = isXfive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getRootCert() {
        return rootCert;
    }

    public void setRootCert(String rootCert) {
        this.rootCert = rootCert;
    }

    public String getServerCert() {
        return serverCert;
    }

    public void setServerCert(String serverCert) {
        this.serverCert = serverCert;
    }

    public String getKeyCert() {
        return keyCert;
    }

    public void setKeyCert(String keyCert) {
        this.keyCert = keyCert;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
