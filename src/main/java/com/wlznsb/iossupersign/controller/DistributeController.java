package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.DistributeDao;
import com.wlznsb.iossupersign.dao.PackStatusDao;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.entity.PackStatus;
import com.wlznsb.iossupersign.service.DistrbuteService;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.RuntimeExec;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;


import org.apache.catalina.DistributedManager;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/distribute")
@Validated
@Slf4j
public class DistributeController {

    @Autowired
    private DistrbuteService distrbuteService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DistributeDao distributeDao;

    @Autowired
    private PackStatusDao packStatusDao;

    //获取描述文件
    @GetMapping
    @RequestMapping("/getMobile")
    public void getMobile(HttpServletRequest request, HttpServletResponse response, @RequestParam String id,@RequestParam String name) throws IOException {
        //域名
        String tempContextUrl = ServerUtil.getRootUrl(request);
        String keyPath = new File("/sign/mode/cert/key.key").getAbsolutePath();
        String rootPath = new File("/sign/mode/cert/root.crt").getAbsolutePath();
        String serverPath = new File("/sign/mode/cert/server.crt").getAbsolutePath();;
        //模板
        String moblicPath =new File("/sign/mode/udid.mobileconfig").getAbsolutePath();
        //随机
        Long round = new Date().getTime();
        //未签名
        String moblicNoSignPath = new File("/sign/mode/temp/" + round + "no.mobileconfig").getAbsolutePath();
        String temp = IoHandler.readTxt(moblicPath);
        temp = temp.replace("urlRep", tempContextUrl + "distribute/getUdid?id=" + id);
        temp = temp.replace("nameRep",name + " -- 点击右上角安装");
        IoHandler.writeTxt(moblicNoSignPath, temp);
        //已签名
        String moblicSignPath = new File("/sign/mode/temp/" + round + ".mobileconfig").getAbsolutePath();
        String cmd =" openssl smime -sign -in " + moblicNoSignPath + " -out " + moblicSignPath + " -signer " + serverPath + " -inkey " + keyPath + " -certfile " + rootPath + " -outform der -nodetach ";
        RuntimeExec.runtimeExec(cmd);
        log.info(tempContextUrl);
        response.sendRedirect(tempContextUrl + round + ".mobileconfig");
    }

    @RequestMapping(value = "/down/{data}",method = RequestMethod.GET)
    public String getDown(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String data) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String tempContextUrl = ServerUtil.getRootUrl(request);
        //base64解码
        String pageData = new String(Base64.getDecoder().decode(data));
        //转换成json
        JsonNode jsonData =  new ObjectMapper().readTree(pageData);
        String name = jsonData.get("data").get("name").asText();
        String id = jsonData.get("data").get("id").asText();
        String account = jsonData.get("data").get("account").asText();
        model.addAttribute("name", name);
        model.addAttribute("size", jsonData.get("data").get("size").asText());
        model.addAttribute("icon", jsonData.get("data").get("icon").asText());
        model.addAttribute("android", tempContextUrl + "distribute/" +"getMobile?id=" + id + "&name=" + name);
        model.addAttribute("ios",tempContextUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + name);
        model.addAttribute("pro", tempContextUrl + "app.mobileprovision");
        return "down";
    }

    /**
     * 获取下载状态
     * @param model
     * @param request
     * @param response
     * @param data
     * @param uuid
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/downStatus/{data}/{uuid}",method = RequestMethod.GET)
    public String getDownStatus(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String data, @PathVariable String uuid) throws JsonProcessingException, UnsupportedEncodingException {
        //域名
        String tempContextUrl = ServerUtil.getRootUrl(request);
        //base64解码
        String pageData = new String(Base64.getDecoder().decode(data));
        //转换成json
        JsonNode jsonData =  new ObjectMapper().readTree(pageData);
        String name = jsonData.get("data").get("name").asText();
        String id = jsonData.get("data").get("id").asText();
        String account = jsonData.get("data").get("account").asText();
        model.addAttribute("name", name);
        model.addAttribute("size", jsonData.get("data").get("size").asText());
        model.addAttribute("icon", jsonData.get("data").get("icon").asText());
        model.addAttribute("android", tempContextUrl + "distribute/" +"getMobile?id=" + id + "&name=" + name);
        model.addAttribute("ios",tempContextUrl + "/distribute/" +"getMobile?id=" + id + "&name=" + name);
        model.addAttribute("pro", tempContextUrl + "app.mobileprovision");
        model.addAttribute("uuid", uuid);
        return "downStatus";
    }


    //301回调
    @RequestMapping(value = "/getUdid")
    public void getUdid(int id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        //创建打包uuid
        String uuid = ServerUtil.getUuid();
        /**
         * 这里的返回值是pist没用上
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                distrbuteService.getUuid(id,uuid, request, response);
            }
        }).start();
        //获取域名
        String url = ServerUtil.getRootUrl(request);
        //获取原来的分发地址
        Distribute distribute =  distributeDao.query(id);
        String skipUrl = distribute.getUrl().replace("down", "downStatus");
        //再次请求带上uuid
        response.setHeader("Location", skipUrl + "/" + uuid);
        response.setStatus(301);
    }

    //查询打包状态
    @RequestMapping(value = "/getStatus")
    @ResponseBody
    public Map<String,Object> getStatus(String uuid,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        PackStatus packStatus =  packStatusDao.query(uuid);
        map.put("code", 0);
        map.put("message", "查询成功");
        map.put("data", packStatus);
        return map;
    }

    //上传ipa
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestParam MultipartFile ipa, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        Distribute distribute = distrbuteService.uploadIpa(ipa, request);
        map.put("code", 0);
        map.put("message", "上传成功");
        map.put("data", distribute);
        return map;
    }



}

