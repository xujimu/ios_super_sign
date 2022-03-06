package com.wlznsb.iossupersign.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: xjm
 * @Date: 2022/03/05/17:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTimeLockAns {


    @ApiModelProperty(value = "过期弹出提示")
    private String toast;

    @ApiModelProperty(value = "过期时间")
    private Date timeLockFinish;

    @ApiModelProperty(value = "是否到期 1到期 0未到期")
    private Integer status;

}
