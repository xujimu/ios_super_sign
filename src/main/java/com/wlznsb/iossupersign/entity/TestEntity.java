package com.wlznsb.iossupersign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName test
 */
@TableName(value ="test")
@Data
public class TestEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}