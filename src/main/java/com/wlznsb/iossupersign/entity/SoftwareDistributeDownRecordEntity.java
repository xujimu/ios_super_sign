package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * mdm企业分发 下载详细记录
 * @TableName software_distribute_down_record
 */
@TableName(value ="software_distribute_down_record")
@Data
public class SoftwareDistributeDownRecordEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "record_id")
    private String recordId;

    /**
     * appid
     */
    @TableField(value = "app_id")
    private Integer appId;

    /**
     * app名字
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * app包名
     */
    @TableField(value = "app_page_name")
    private String appPageName;

    /**
     * 下载ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 下载时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 用户账号
     */
    @TableField(value = "account")
    private String account;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}