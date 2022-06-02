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
 * @TableName pack_status_api
 */
@TableName(value ="pack_status_api")
@Data
public class PackStatusApiEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "task_id")
    private String taskId;

    /**
     * 
     */
    @TableField(value = "account")
    private String account;

    /**
     * 
     */
    @TableField(value = "udid")
    private String udid;

    /**
     * 
     */
    @TableField(value = "app_id")
    private Integer appId;

    /**
     * 
     */
    @TableField(value = "ipa_url")
    private String ipaUrl;

    /**
     * 
     */
    @TableField(value = "plist_url")
    private String plistUrl;

    /**
     * 
     */
    @TableField(value = "install_url")
    private String installUrl;

    /**
     * 
     */
    @TableField(value = "status_log")
    private String statusLog;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 状态 -0排队 1处理中 2成功 3失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 
     */
    @TableField(value = "cert_id")
    private Integer certId;

    /**
     * 
     */
    @TableField(value = "cert_down_url")
    private String certDownUrl;

    /**
     * 
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 
     */
    @TableField(value = "callback_url")
    private String callbackUrl;

    /**
     * 
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否请求成功
     */
    @TableField(value = "callback_status")
    private Integer callbackStatus;

    /**
     * 签名类型 1 新签 2重签名
     */
    @TableField(value = "sign_type")
    private Integer signType;

    /**
     * 重签的任务id
     */
    @TableField(value = "restart_sign_task_id")
    private String restartSignTaskId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}