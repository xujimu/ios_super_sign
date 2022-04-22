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
 * @TableName mdm_super_update_ipa_task
 */
@TableName(value ="mdm_super_update_ipa_task")
@Data
public class MdmSuperUpdateIpaTaskEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "uuid")
    private String uuid;

    /**
     * 打包任务id
     */
    @TableField(value = "pack_status_id")
    private String packStatusId;

    /**
     * 状态 待处理 已处理 失败
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

    /**
     * 
     */
    @TableField(value = "plist_url")
    private String plistUrl;

    /**
     * 任务id
     */
    @TableField(value = "task_id")
    private String taskId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}