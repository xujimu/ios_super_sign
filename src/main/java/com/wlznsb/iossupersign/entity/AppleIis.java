package com.wlznsb.iossupersign.entity;

import org.apache.ibatis.annotations.Insert;

import java.util.Date;

/**
 * 苹果账号实体
 *
 *
 */
public class AppleIis {
    private Integer id;
    private String account;
    private String iis;
    private String kid;
    private String certId;
    private String identifier;
    private String p8;
    private String p12;
    private Integer start;
    private Integer status;
    private Integer ispublic;
    private Integer count;
    private Date createTime;

    public AppleIis(Integer id, String account, String iis, String kid, String certId, String identifier, String p8, String p12, Integer start, Integer status, Integer ispublic, Integer count, Date createTime) {
        this.id = id;
        this.account = account;
        this.iis = iis;
        this.kid = kid;
        this.certId = certId;
        this.identifier = identifier;
        this.p8 = p8;
        this.p12 = p12;
        this.start = start;
        this.status = status;
        this.ispublic = ispublic;
        this.count = count;
        this.createTime = createTime;
    }

    public AppleIis() {
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

    public String getIis() {
        return iis;
    }

    public void setIis(String iis) {
        this.iis = iis;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getP8() {
        return p8;
    }

    public void setP8(String p8) {
        this.p8 = p8;
    }

    public String getP12() {
        return p12;
    }

    public void setP12(String p12) {
        this.p12 = p12;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AppleIis{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", iis='" + iis + '\'' +
                ", kid='" + kid + '\'' +
                ", certId='" + certId + '\'' +
                ", identifier='" + identifier + '\'' +
                ", p8='" + p8 + '\'' +
                ", p12='" + p12 + '\'' +
                ", start=" + start +
                ", status=" + status +
                ", ispublic=" + ispublic +
                ", count=" + count +
                ", createTime=" + createTime +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
