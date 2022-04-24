package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 执行命令任务
 * @TableName device_command_task
 */
@TableName(value ="device_command_task")
@Data
public class DeviceCommandTaskEntity implements Serializable {


    //分钟
    public static final int 重试_时间_间隔 = 10;

    public static final int 任务_等待_执行 = 0;
    public static final int 任务_唤醒_命令_已发送 = 1;
    public static final int 任务_命令_已发送 = 2;
    public static final int 任务_命令_执行成功 = 3;
    public static final int 任务_命令_执行失败 = 4;


    public static final int 唤醒_设备_最大值 = 3;




    /**
     * id
     */
    @TableId(value = "task_id")
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 命令
     */
    @TableField(value = "cmd")
    @ApiModelProperty(value = "命令")
    private String cmd;

    /**
     * 执行返回结果集合
     */
    @TableField(value = "exec_result")
    @ApiModelProperty(value = "执行返回结果集合")
    private String execResult;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 执行时间
     */
    @TableField(value = "exec_time")
    @ApiModelProperty(value = "执行时间")
    private Date execTime;

    /**
     * 返回时间
     */
    @TableField(value = "result_time")
    @ApiModelProperty(value = "执行结果返回时间")
    private Date resultTime;

    /**
     *  0 任务_等待_执行 1 任务_唤醒_命令_已发送 2 任务_命令_已发送 3任务_命令_执行成功 4 任务_命令_执行失败
     */
    @TableField(value = "task_status")
    @ApiModelProperty(value = "0 任务_等待_执行 1 任务_唤醒_命令_已发送 2 任务_命令_已发送 3任务_命令_执行成功 4 任务_命令_执行失败")
    private Integer taskStatus;

    /**
     * 唤醒次数 如果在指定时间没有回应则会重试 重试唤醒次数会增加
     */
    @TableField(value = "push_count")
    @ApiModelProperty(value = "唤醒次数 如果在指定时间没有回应则会重试 重试唤醒次数会增加")
    private Integer pushCount;

    /**
     * 命令执行返回状态码
     */
    @TableField(value = "exec_result_status")
    @ApiModelProperty(value = "命令执行返回状态码")
    private String execResultStatus;


    /**
     * cmd命令的其他参数 json 比如安装app就有其他参数
     */
    @TableField(value = "cmd_append")
    @ApiModelProperty(value = "cmd命令的其他参数 json 比如安装app就有其他参数")
    private String cmdAppend;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 证书id
     */
    @TableField(value = "cert_id")
    @ApiModelProperty(value = "证书id")
    private String certId;

    /**
     * 设备id
     */
    @TableField(value = "udid")
    @ApiModelProperty(value = "设备udi")
    private String udid;


    @TableField(exist = false)
    private String p12Path;

    @TableField(exist = false)
    private String p12Password;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private String magic;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}