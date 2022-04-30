package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * 设备id是否可控
 *
 * @TableName device_status
 */
@TableName(value ="device_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatusEntity implements Serializable {


    //设备可控
    public static final int STATUS_ON = 1;
    //设备不可控
    public static final int STATUS_OFF = 0;


    /**
     * 设备id
     */
    @TableId(value = "device_id")
    private String deviceId;

    /**
     * 0不可控 1可控
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 设备udid
     */
    @TableField(value = "udid")
    private String udid;

    /**
     * 证书id
     */
    @TableField(value = "cert_id")
    private String certId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}