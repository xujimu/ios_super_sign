package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName systemctl_settings
 */
@TableName(value ="systemctl_settings")
@Data
public class SystemctlSettingsEntity implements Serializable {
    /**
     *  mdm企业签第几次下载触发
     */
    @TableField(value = "mdm_soft_num")
    private Integer mdmSoftNum;

    /**
     * 额外扣除扣除数量
     */
    @TableField(value = "mdm_soft_re_count")
    private Integer mdmSoftReCount;

    /**
     *  mdm超级签第几次下载触发
     */
    @TableField(value = "mdm_super_num")
    private Integer mdmSuperNum;

    /**
     * 扣除数量
     */
    @TableField(value = "mdm_super_re_count")
    private Integer mdmSuperReCount;

    /**
     * 超级签第几次下载触发
     */
    @TableField(value = "super_num")
    private Integer superNum;

    /**
     * 扣除数量
     */
    @TableField(value = "super_re_count")
    private Integer superReCount;

    /**
     * 企业签第几次下载触发
     */
    @TableField(value = "soft_num")
    private Integer softNum;

    /**
     * 额外扣除扣除数量
     */
    @TableField(value = "soft_re_count")
    private Integer softReCount;

    /**
     * 超级签所需公有池
     */
    @TableField(value = "super_total")
    private Integer superTotal;

    /**
     * mdm超级签共有池
     */
    @TableField(value = "mdm_super_total")
    private Integer mdmSuperTotal;

    /**
     * 企业分发公有池
     */
    @TableField(value = "soft_total")
    private Integer softTotal;

    /**
     * mdm企业分发公有池
     */
    @TableField(value = "mdm_soft_total")
    private Integer mdmSoftTotal;

    /**
     * 网页打包所需公有池
     */
    @TableField(value = "web_pack_total")
    private Integer webPackTotal;

    /**
     * 单点分发扣除次数
     */
    @TableField(value = "one_super_total")
    private Integer oneSuperTotal;

    /**
     * 免签打包域名
     */
    @TableField(value = "mq_domain")
    private String mqDomain;

    /**
     * mdm域名
     */
    @TableField(value = "mdm_domain")
    private String mdmDomain;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}