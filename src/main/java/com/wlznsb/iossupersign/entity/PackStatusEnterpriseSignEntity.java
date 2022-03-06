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
 * @TableName pack_status_enterprise_sign
 */
@TableName(value ="pack_status_enterprise_sign")
@Data
public class PackStatusEnterpriseSignEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 
     */
    @TableField(value = "cert_id")
    private Integer certId;

    /**
     * 
     */
    @TableField(value = "cert_name")
    private String certName;

    /**
     * 创建者
     */
    @TableField(value = "account")
    private String account;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * app名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 包名
     */
    @TableField(value = "page_name")
    private String pageName;

    /**
     * 版本
     */
    @TableField(value = "version")
    private String version;

    /**
     * 打包状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 下载地址
     */
    @TableField(value = "down_url")
    private String downUrl;

    /**
     * 
     */
    @TableField(value = "ipa_path")
    private String ipaPath;

    /**
     * 
     */
    @TableField(value = "url")
    private String url;

    /**
     * 是否开启时间锁 0关1开
     */
    @TableField(value = "is_time_lock")
    private Integer isTimeLock;

    /**
     * 到期时间
     */
    @TableField(value = "lock_time_finish")
    private Date lockTimeFinish;

    /**
     * 时间锁请求url
     */
    @TableField(value = "lock_request_url")
    private String lockRequestUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}