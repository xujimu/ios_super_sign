package com.wlznsb.iossupersign.entity;


import lombok.*;

import java.util.Date;

/**
 * ipa自助分发证书
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IosSignUdidCert {

    private String certId;
    private String account;
    private String p12Path;
    private String mobileprovisionPath;
    private String p12Password;
    private String udid;
    private String introduce;
    private Date createTime;


}
