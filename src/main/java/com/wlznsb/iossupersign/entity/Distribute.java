package com.wlznsb.iossupersign.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.ref.PhantomReference;
import java.util.Date;

/**
 * 分发应用信息和位置
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distribute {

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
    private String images;
    private Integer downCode;
    private String buyDownCodeUrl;
    private String language;



}
