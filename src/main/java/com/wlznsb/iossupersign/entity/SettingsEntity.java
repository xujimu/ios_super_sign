package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 设置
 * @TableName settings
 */
@TableName(value ="settings")
@Data
public class SettingsEntity implements Serializable {
    /**
     * 时间锁请求url
     */
    @TableField(value = "time_lock_request_url")
    private String timeLockRequestUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}