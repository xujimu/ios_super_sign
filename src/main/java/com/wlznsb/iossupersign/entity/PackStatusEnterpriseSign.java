package com.wlznsb.iossupersign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 企业签名打包状态
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackStatusEnterpriseSign {

    private String id;
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
    private Integer isTimeLock;
    private Date lockTimeFinish;
    private String lockRequestUrl;

}
