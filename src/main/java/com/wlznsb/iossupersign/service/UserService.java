package com.wlznsb.iossupersign.service;

import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserService {

    /**
     * 注册用户
     * @param user
     * @return
     */
    UserDto register(User user);

    /**
     * 用户登陆
     * @param account
     * @param password
     * @return
     */
    UserDto login(String account,String password);


    /**
     * 删除用户
     * @param account
     * @return
     */
    UserDto dele(String account);


    /**
     * 修改密码
     * @param account
     * @param password
     * @return
     */
    UserDto updatePassword(String account,String password);


    /**
     * 修改类型
     * @param account
     * @param type
     * @return
     */
    UserDto updateType(String account,int type);

}
