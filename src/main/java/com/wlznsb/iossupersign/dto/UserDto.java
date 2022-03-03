package com.wlznsb.iossupersign.dto;

import com.wlznsb.iossupersign.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 用户service返回值
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDto {
    private int code;
    private String message;
    private User user;


}
