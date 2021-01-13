package com.wlznsb.iossupersign.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.AppleIis;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.AppleIisServiceImpl;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
@Validated
@CrossOrigin(allowCredentials="true")
public class AdminController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AppleIisServiceImpl appleIisService;


    //修改类型
    @RequestMapping(value = "/updateType",method = RequestMethod.POST)
    public Map<String,Object> updateType(@RequestParam @NotEmpty String account, @RequestParam @NotEmpty @Range(max = 1,min = 0) int type, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        userService.updateType(account, type);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //删除用户
    @RequestMapping(value = "/dele",method = RequestMethod.POST)
    public Map<String,Object> dele(@RequestParam @NotEmpty String account){
        Map<String,Object> map = new HashMap<String, Object>();
        userService.dele(account);
        map.put("code", 0);
        map.put("message", "删除成功");
        return map;
    }


    /**
     * 查询所有的iis证书
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryIisAll",method = RequestMethod.GET)
    public Map<String,Object> queryIisAll(HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page =  (Page) appleIisService.queryAll();
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }


    /**
     * 添加共有池
     * @param request
     * @param account
     * @param count 添加数量
     * @return
     */
    @RequestMapping(value = "/addUserCount",method = RequestMethod.POST)
    public Map<String,Object> addUserCount(HttpServletRequest request,@RequestParam  String account,@RequestParam  Integer count){
        Map<String,Object> map = new HashMap<String, Object>();
        userDao.addCount(account, count);
        map.put("code", 0);
        map.put("message", "操作成功");
        return map;
    }


    /**
     * 查询所有用户没用业务
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryAll",method = RequestMethod.GET)
    public Map<String,Object> queryAll(HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        PageHelper.startPage(pageNum,pageSize);
        Page<User> page =  (Page) userDao.queryAll();
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

}
