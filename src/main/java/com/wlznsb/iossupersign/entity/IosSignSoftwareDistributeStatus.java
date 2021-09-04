package com.wlznsb.iossupersign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 单点分发打包状态
 *
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IosSignSoftwareDistributeStatus {
    private String uuid;
    private String account;
    private String iosId;
    private String certId;
    private String appName;
    private String appVersion;
    private String pageName;
    private String downUrl;
    private String status;
    private Date createTime;
    private Date updateTime;


}
