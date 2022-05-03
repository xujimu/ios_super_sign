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
 * @TableName pack_status
 */
@TableName(value ="pack_status")
@Data
public class PackStatusEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "account")
    private String account;

    /**
     * 
     */
    @TableField(value = "page_name")
    private String pageName;

    /**
     * 
     */
    @TableField(value = "uuid")
    private String uuid;

    /**
     * 
     */
    @TableField(value = "udid")
    private String udid;

    /**
     * 
     */
    @TableField(value = "iis")
    private String iis;

    /**
     * 
     */
    @TableField(value = "p12_path")
    private String p12Path;

    /**
     * 
     */
    @TableField(value = "mobile_path")
    private String mobilePath;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "ipa")
    private String ipa;

    /**
     * 
     */
    @TableField(value = "plist")
    private String plist;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "sign_off")
    private Integer signOff;

    /**
     * 
     */
    @TableField(value = "app_id")
    private Integer appId;

    /**
     * 
     */
    @TableField(value = "url")
    private String url;

    /**
     * 
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 
     */
    @TableField(value = "down_code")
    private String downCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}