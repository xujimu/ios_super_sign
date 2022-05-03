package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName ios_sign_software_distribute_status
 */
@TableName(value ="ios_sign_software_distribute_status")
@Data
public class IosSignSoftwareDistributeStatusEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "uuid")
    private String uuid;

    /**
     * 
     */
    @TableField(value = "account")
    private String account;

    /**
     * 
     */
    @TableField(value = "ios_id")
    private String iosId;

    /**
     * 
     */
    @TableField(value = "cert_id")
    private String certId;

    /**
     * 
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 
     */
    @TableField(value = "app_version")
    private String appVersion;

    /**
     * 
     */
    @TableField(value = "page_name")
    private String pageName;

    /**
     * 
     */
    @TableField(value = "down_url")
    private String downUrl;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}