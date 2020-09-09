package com.wlznsb.iossupersign.controller;


import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@Validated
@CrossOrigin(allowCredentials="true")
public class UserController {

    @Autowired
    private UserService userService;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Map<String,Object> login(@RequestParam @NotEmpty String account, @RequestParam @NotEmpty String password, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        UserDto userDto = userService.login(account, password);
        User user = userDto.getUser();
        request.getSession().setAttribute("user", user);
        user.setPassword(null);
        map.put("code", 0);
        map.put("message", "登陆成功");
        map.put("data", user);
        return map;
    }

    //注册
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

    //退出
    @RequestMapping(value = "/quit",method = RequestMethod.GET)
    public Map<String,Object> quit(HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        request.getSession().removeAttribute("user");
        map.put("code", 0);
        map.put("message", "退出成功");
        return map;
    }
    //修改密码
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public Map<String,Object> updatePassword(@RequestParam @NotEmpty String password,@RequestParam @NotEmpty String newPassword,HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("user");
        userService.updatePassword(user.getAccount(),password,newPassword);
        request.getSession().removeAttribute("user");
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

}
