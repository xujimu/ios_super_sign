package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 注册的设备信息
 * @TableName device_info
 */
@TableName(value ="device_info")
@Data
public class DeviceInfoEntity implements Serializable {
    /**
     * 设备唯一id
     */
    @TableId(value = "device_id")
    private String deviceId;

    /**
     * 证书id
     */
    @TableField(value = "cert_id")
    private String certId;

    /**
     * 设备token
     */
    @TableField(value = "token")
    private String token;

    /**
     * 设备uuid
     */
    @TableField(value = "udid")
    private String udid;

    /**
     * 给设备发送命令配置文件里需要用到
     */
    @TableField(value = "unlock_token")
    private String unlockToken;

    /**
     * 向apns服务器唤醒设备的时候需要
     */
    @TableField(value = "magic")
    private String magic;

    /**
     * 证书的topic
     */
    @TableField(value = "topic")
    private String topic;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 设备状态 已卸载CheckOut 注册中Authenticate 更新TokenUpdate  
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}