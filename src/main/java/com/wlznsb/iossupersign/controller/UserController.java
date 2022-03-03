package com.wlznsb.iossupersign.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.mapper.DomainDao;
import com.wlznsb.iossupersign.mapper.PackStatusDao;
import com.wlznsb.iossupersign.dto.UserDto;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@Validated
@CrossOrigin(allowCredentials="true")
@PxCheckLogin
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PackStatusDao packStatusDao;


    @Autowired
    private DomainDao domainDao;



    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @PxCheckLogin(value = false)
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


    @RequestMapping(value = "/queryDomain",method = RequestMethod.GET)
    public Map<String,Object> queryDomain(HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page =  (Page)  domainDao.queryAll();;
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    //注册
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @PxCheckLogin(value = false)
    public Map<String,Object> register(@RequestParam @NotEmpty String account,@RequestParam @NotEmpty String password){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = new User(null,account,password,new Date(),0,0,null);
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
    public Map<String,Object> updatePassword(String token,@RequestParam @NotEmpty String password,@RequestParam @NotEmpty String newPassword,HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        userService.updatePassword(user.getAccount(),password,newPassword);
        request.getSession().removeAttribute("user");
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //查询下载记录
    @RequestMapping(value = "/queryDown",method = RequestMethod.GET)
    public Map<String,Object> queryDown(String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page =  (Page) packStatusDao.queryDown(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

}
