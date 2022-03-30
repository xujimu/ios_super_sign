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
 * @TableName apple_iis
 */
@TableName(value ="apple_iis")
@Data
public class AppleIisEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "account")
    private String account;

    /**
     * 
     */
    @TableField(value = "iis")
    private String iis;

    /**
     * 
     */
    @TableField(value = "kid")
    private String kid;

    /**
     * 
     */
    @TableField(value = "cert_id")
    private String certId;

    /**
     * 
     */
    @TableField(value = "identifier")
    private String identifier;

    /**
     * 
     */
    @TableField(value = "p8")
    private String p8;

    /**
     * p8文件路径
     */
    @TableField(value = "p12")
    private String p12;

    /**
     * 0不启用,1启用
     */
    @TableField(value = "start")
    private Integer start;

    /**
     * 0失效,未失效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0私,1公
     */
    @TableField(value = "ispublic")
    private Integer ispublic;

    /**
     * 
     */
    @TableField(value = "count")
    private Integer count;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}