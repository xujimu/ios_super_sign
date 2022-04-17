package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 执行命令任务
 * @TableName device_command_task
 */
@TableName(value ="device_command_task")
@Data
public class DeviceCommandTaskEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "task_id")
    private String taskId;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    private String deviceId;

    /**
     * 命令 
     */
    @TableField(value = "cmd")
    private String cmd;

    /**
     * 执行返回结果
     */
    @TableField(value = "exec_result")
    private String execResult;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 执行时间
     */
    @TableField(value = "exec_time")
    private Date execTime;

    /**
     * 返回时间
     */
    @TableField(value = "result_time")
    private Date resultTime;

    /**
     * 0 任务_等待_执行 1 任务_唤醒_命令_已发送 2 任务_命令_已发送 3任务_命令_执行成功 4 任务_命令_执行失败
     */
    @TableField(value = "task_status")
    private Integer taskStatus;

    /**
     * 唤醒次数 如果在指定时间没有回应则会重试 
     */
    @TableField(value = "push_count")
    private Integer pushCount;

    /**
     * 执行返回状态码
     */
    @TableField(value = "exec_result_status")
    private String execResultStatus;

    /**
     * cmd命令的其他参数 json 比如安装app就有其他参数
     */
    @TableField(value = "cmd_append")
    private String cmdAppend;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}