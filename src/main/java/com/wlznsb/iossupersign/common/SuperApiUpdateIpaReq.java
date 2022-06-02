package com.wlznsb.iossupersign.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuperApiUpdateIpaReq {

    @ApiModelProperty(value = "ipa文件")
    MultipartFile ipa;

    @ApiModelProperty(value = "应用id")
    String id;
}
