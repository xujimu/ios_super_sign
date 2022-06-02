package com.wlznsb.iossupersign.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuperApiAddIpaReq {

    @ApiModelProperty(value = "ipa文件")
    MultipartFile ipa;


}
