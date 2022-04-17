package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * mdm证书表
 * @TableName cert_info
 */
@TableName(value ="cert_info")
@Data
public class CertInfoEntity implements Serializable {
    /**
     * 证书id
     */
    @TableId(value = "cert_id")
    private String certId;

    /**
     * 证书路径 如 ./data/p12.p12
     */
    @TableField(value = "p12_path")
    private String p12Path;

    /**
     * 证书名
     */
    @TableField(value = "cert_name")
    private String certName;

    /**
     * 证书密码
     */
    @TableField(value = "p12_password")
    private String p12Password;

    /**
     * 状态 1正常 0失效 
     */
    @TableField(value = "cert_status")
    private Integer certStatus;

    /**
     * 证书信息 topic 推送需要使用
     */
    @TableField(value = "topic")
    private String topic;

    /**
     * 证书信息serial_number
     */
    @TableField(value = "serial_number")
    private String serialNumber;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 证书有效期起始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 证书有效期结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 配置文件路径
     */
    @TableField(value = "mobile_config_path")
    private String mobileConfigPath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}