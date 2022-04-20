package com.wlznsb.iossupersign.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 自助分发
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareDistribute {

    private Integer id;
    private String account;
    private String appName;
    private String pageName;
    private String version;
    private String icon;
    private String ipa;
    private String apk;
    private String url;
    private Date createTime;
    private String introduce;
    private String uuid;
    private String language;


}
