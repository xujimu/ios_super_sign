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
 * @TableName mdm_distribute
 */
@TableName(value ="mdm_distribute")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MdmDistributeEntity implements Serializable {
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
    @TableField(value = "icon")
    private String icon;

    /**
     *
     */
    @TableField(value = "ipa")
    private String ipa;

    /**
     *
     */
    @TableField(value = "apk")
    private String apk;

    /**
     *
     */
    @TableField(value = "url")
    private String url;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "introduce")
    private String introduce;

    /**
     *
     */
    @TableField(value = "images")
    private String images;

    /**
     * 下载码,0不启用,1启用
     */
    @TableField(value = "down_code")
    private Integer downCode;

    /**
     *
     */
    @TableField(value = "buy_down_code_url")
    private String buyDownCodeUrl;

    /**
     *
     */
    @TableField(value = "language")
    private String language;

    /**
     *
     */
    @TableField(exist = false)
    private Integer sumCount;

    /**
     *
     */
    @TableField(exist = false)
    private Integer dayCount;

    /**
     *
     */
    @TableField(exist = false)
    private Integer lastDayCount;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}