package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckAdmin;
import com.wlznsb.iossupersign.mapper.DomainDao;
import com.wlznsb.iossupersign.mapper.UserDao;
import com.wlznsb.iossupersign.entity.Domain;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.service.AppleIisServiceImpl;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import com.wlznsb.iossupersign.util.SettingUtil;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
@Validated
@CrossOrigin(allowCredentials="true")
@PxCheckAdmin
public class AdminController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AppleIisServiceImpl appleIisService;

    @Autowired
    private DomainDao domainDao;



    //修改类型
    @RequestMapping(value = "/updateType",method = RequestMethod.POST)
    public Map<String,Object> updateType(@RequestParam @NotEmpty String account, @RequestParam @NotEmpty @Range(max = 1,min = 0) int type, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        userService.updateType(account, type);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }

    //修改设置
    @RequestMapping(value = "/updateSetting",method = RequestMethod.POST)
    public Map<String,Object> updateIpaDownUrl(@RequestParam  String  settingJson, HttpServletRequest request) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<String, Object>();
        JsonNode jsonNode = new ObjectMapper().readTree(settingJson);
        SettingUtil.ipaDownUrl = jsonNode.get("IpaDownUrl").asText();
        System.out.println(SettingUtil.ipaDownUrl);
        map.put("code", 0);
        map.put("message", "修改成功");
        return map;
    }


    //查询设置
    @RequestMapping(value = "/querySetting",method = RequestMethod.GET)
    public Map<String,Object> querySetting(HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> setting = new HashMap<String, Object>();
        setting.put("IpaDownUrl",SettingUtil.ipaDownUrl);
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", setting);
        return map;
    }


    //添加域名
    @RequestMapping(value = "/addDomain",method = RequestMethod.POST)
    public Map<String,Object> addDomain(@RequestParam @NotEmpty String domain, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        domainDao.add(new Domain(null,domain,new Date(),1));
        map.put("code", 0);
        map.put("message", "添加成功");
        return map;
    }

    //删除域名
    @RequestMapping(value = "/deleteDomain",method = RequestMethod.POST)
    public Map<String,Object> deleteDomain(@RequestParam  Integer id, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        domainDao.dele(id);
        map.put("code", 0);
        map.put("message", "删除成功");
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
