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
 * @TableName mdm_software_distribute_down_record
 */
@TableName(value ="mdm_software_distribute_down_record")
@Data
public class MdmSoftwareDistributeDownRecordEntity implements Serializable {
    /**
     * 
     */
    @TableField(value = "device_id")
    private String deviceId;

    /**
     * 
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "udid")
    private String udid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}