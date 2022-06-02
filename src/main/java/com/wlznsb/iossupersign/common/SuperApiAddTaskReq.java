package com.wlznsb.iossupersign.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuperApiAddTaskReq {

    @ApiModelProperty(value = "udid")
    String udid;


    @ApiModelProperty(value = "签名类型 1新签 2重签名")
    String sign_type;

    @ApiModelProperty(value = "如果是重新签名 重新签名的任务id")
    String taskId;


    @ApiModelProperty(value = "需要签名的应用id")
    String appId;


    @ApiModelProperty(value = "备注")
    String remark;
}
