package com.wlznsb.iossupersign.entity;


import lombok.*;

import java.util.Date;

/**
 * ipa自助分发
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IosSignSoftwareDistribute {

    private String iosId;
    private String account;
    private String appName;
    private String pageName;
    private String version;
    private String icon;
    private String ipa;
    private String apk;
    private String url;
    private String certId;
    private Date createTime;
    private String introduce;
    private Integer autoPageName;

}
