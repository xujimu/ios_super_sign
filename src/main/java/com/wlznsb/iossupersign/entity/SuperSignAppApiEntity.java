package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName super_sign_app_api
 */
@TableName(value ="super_sign_app_api")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperSignAppApiEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 
     */
    @TableField(value = "account")
    private String account;

    /**
     * 
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 
     */
    @TableField(value = "page_name")
    private String pageName;

    /**
     * 
     */
    @TableField(value = "version")
    private String version;

    /**
     * 
     */
    @TableField(value = "icon_url")
    private String iconUrl;

    /**
     * 
     */
    @TableField(value = "ipa_path")
    private String ipaPath;

    /**
     * 
     */
    @TableField(value = "down_url")
    private String downUrl;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}