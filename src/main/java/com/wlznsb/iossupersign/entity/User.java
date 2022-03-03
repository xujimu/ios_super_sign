package com.wlznsb.iossupersign.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private Integer id;
    private String account;
    private String password;
    private Date createTime;
    private Integer type;
    private Integer count;

    private String token;


}
