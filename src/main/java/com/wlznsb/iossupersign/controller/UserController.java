package com.wlznsb.iossupersign.controller;

import com.sun.istack.internal.NotNull;
import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,Object> login(@RequestParam @NotEmpty String account, @RequestParam @NotEmpty String password){
        Map<String,Object> map = new HashMap<String, Object>();
        UserDto userDto = userService.login(account, password);
        User user = userDto.getUser();
        user.setPassword(null);
        map.put("code", 0);
        map.put("message", "登陆成功");
        map.put("data", user);
        return map;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Map<String,Object> register(@RequestParam @NotEmpty String account,@RequestParam @NotEmpty String password){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = new User(null,account,password,new Date(),0);
        UserDto userDto = userService.register(user);
        user.setPassword(null);
        map.put("code", 0);
        map.put("message", "注册成功");
        map.put("data", user);
        return map;
    }





}
