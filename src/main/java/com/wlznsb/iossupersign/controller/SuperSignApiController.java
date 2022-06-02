package com.wlznsb.iossupersign.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlznsb.iossupersign.annotation.PxCheckLogin;
import com.wlznsb.iossupersign.common.SuperApiAddIpaReq;
import com.wlznsb.iossupersign.common.SuperApiAddTaskReq;
import com.wlznsb.iossupersign.common.SuperApiUpdateIpaReq;
import com.wlznsb.iossupersign.entity.*;
import com.wlznsb.iossupersign.mapper.*;
import com.wlznsb.iossupersign.service.AppleIisServiceImpl;
import com.wlznsb.iossupersign.service.DistrbuteServiceImpl;
import com.wlznsb.iossupersign.service.UserServiceImpl;
import com.wlznsb.iossupersign.util.*;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Controller
@RequestMapping(value = "/super_sign_api")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
@PxCheckLogin
public class SuperSignApiController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AppleIisServiceImpl appleIisService;

    @Autowired
    private AppleIisMapper iisMapper;


    @Autowired
    private SuperSignAppApiMapper superSignAppApiMapper;



    /**
     * 查询所有证书
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryAccountIis",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountIis(@RequestHeader String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page<AppleIis> page = (Page) appleIisService.queryAccount(user.getAccount());

        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }

    /**
     * 查询单个证书
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryOneIis",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountOneIis(@RequestParam String id,@RequestHeader String token,HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        AppleIisEntity appleIisEntity = iisMapper.selectById(id);
        if(null == appleIisEntity){
            throw new RuntimeException("证书不存在");
        }

        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", appleIisEntity);
        return map;
    }

    @RequestMapping(value = "/addIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addIpa(SuperApiAddIpaReq req, @RequestHeader String token, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        String id = MyUtil.getUuid();

        try {
            String rootUrl = ServerUtil.getRootUrl(request);



            new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/").mkdirs();
            //icon路径
            String iconPath = new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/" +  id + ".png").getAbsolutePath();
            //ipa路径
            String ipaPath = new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/" +  id + ".ipa").getAbsolutePath();
            //写出
            System.out.println(ipaPath);
            req.getIpa().transferTo(new File(ipaPath));
            //ipa.transferTo(new File(iconPath));
            //读取信息
            Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
            if(mapIpa.get("code") != null){
                throw new RuntimeException("无法读取包信息");
            }
            String name = mapIpa.get("displayName").toString();
            String version = mapIpa.get("versionName").toString();
            String pageName  = mapIpa.get("package").toString();
            //处理图片
            MyUtil.getIpaImg("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id  + "/" + id +  ".png","./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id  + "/" + id +  ".png");

            SuperSignAppApiEntity superSignAppApiEntity = new SuperSignAppApiEntity();
            superSignAppApiEntity.setId(id);
            superSignAppApiEntity.setAppName(name);
            superSignAppApiEntity.setAccount(user.getAccount());
            superSignAppApiEntity.setCreateTime(new Date());
            superSignAppApiEntity.setIconUrl(rootUrl + "super/super_sign_app_api/" + id + "/" + id + ".png");
            superSignAppApiEntity.setIpaPath(ipaPath);
            superSignAppApiEntity.setPageName(pageName);
            superSignAppApiEntity.setDownUrl(rootUrl + "super/super_sign_app_api/" + id + "/" + id + ".ipa");
            superSignAppApiEntity.setVersion(version);
            superSignAppApiEntity.setRemark("");
            superSignAppApiMapper.insert(superSignAppApiEntity);

        }catch (Exception e){
            e.printStackTrace();
            FileUtil.del(new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id).getAbsolutePath());
            throw  new RuntimeException("上传失败" + e.getMessage());
        }

        map.put("code", 0);
        map.put("message", "操作成功");

        return map;
    }

    @RequestMapping(value = "/updateIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateIpa(SuperApiUpdateIpaReq req, @RequestHeader String token, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);

        SuperSignAppApiEntity superSignAppApiEntity = superSignAppApiMapper.selectById(req.getId());

        if(null == superSignAppApiEntity){
            throw new RuntimeException("应用不存在");
        }

        String id = superSignAppApiEntity.getId();

        try {
            String rootUrl = ServerUtil.getRootUrl(request);

            new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/").mkdirs();
            //icon路径
            String iconPath = new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/" +  id + ".png").getAbsolutePath();
            //ipa路径
            String ipaPath = new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id + "/" +  id + ".ipa").getAbsolutePath();
            //写出
            System.out.println(ipaPath);
            req.getIpa().transferTo(new File(ipaPath));
            //ipa.transferTo(new File(iconPath));
            //读取信息
            Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA(ipaPath,iconPath);
            if(mapIpa.get("code") != null){
                throw new RuntimeException("无法读取包信息");
            }
            String name = mapIpa.get("displayName").toString();
            String version = mapIpa.get("versionName").toString();
            String pageName  = mapIpa.get("package").toString();
            //处理图片
            MyUtil.getIpaImg("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id  + "/" + id +  ".png","./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id  + "/" + id +  ".png");

            superSignAppApiEntity.setId(id);
            superSignAppApiEntity.setAppName(name);
            superSignAppApiEntity.setAccount(user.getAccount());
            superSignAppApiEntity.setCreateTime(new Date());
            superSignAppApiEntity.setIconUrl(rootUrl + "super/super_sign_app_api/" + id + "/" + id + ".png");
            superSignAppApiEntity.setIpaPath(ipaPath);
            superSignAppApiEntity.setPageName(pageName);
            superSignAppApiEntity.setDownUrl(rootUrl + "super/super_sign_app_api/" + id + "/" + id + ".ipa");
            superSignAppApiEntity.setVersion(version);
            superSignAppApiEntity.setRemark("");
            superSignAppApiMapper.updateById(superSignAppApiEntity);

        }catch (Exception e){
            e.printStackTrace();
            FileUtil.del(new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id).getAbsolutePath());
            throw  new RuntimeException("上传失败" + e.getMessage());
        }

        map.put("code", 0);
        map.put("message", "操作成功");

        return map;
    }


    @RequestMapping(value = "/deleteIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteIpa(@RequestParam String id, @RequestHeader String token, HttpServletRequest request){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        SuperSignAppApiEntity superSignAppApiEntity = superSignAppApiMapper.selectById(id);
        if(null != superSignAppApiEntity){
            superSignAppApiMapper.deleteById(id);
            FileUtil.del(new File("./sign/temp/" + user.getAccount() + "/super_sign_app_api/" + id).getAbsolutePath());
        }else {
            throw  new RuntimeException("包不存在");
        }
        map.put("code", 0);
        map.put("message", "操作成功");

        return map;
    }


    //查询下载记录
    @RequestMapping(value = "/queryAccountApp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryAccountApp(@RequestHeader String token,HttpServletRequest request,@RequestParam  Integer pageNum,@RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page<SuperSignAppApiEntity> page =  (Page) superSignAppApiMapper.selectByAccount(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }



    @Autowired
    private PackStatusApiMapper packStatusApiMapper;

    /**
     * 添加签名任务
     * @param token
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/addTask",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> addTask(@RequestBody SuperApiAddTaskReq req, @RequestHeader String token, HttpServletRequest request, @RequestParam  Integer pageNum, @RequestParam  Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        User user = userService.getUser(token);
        PageHelper.startPage(pageNum,pageSize);
        Page<SuperSignAppApiEntity> page =  (Page) superSignAppApiMapper.selectByAccount(user.getAccount());
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", page.getResult());
        map.put("pages", page.getPages());
        map.put("total", page.getTotal());
        return map;
    }
}


