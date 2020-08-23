package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.service.DistrbuteService;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.RuntimeExec;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    //获取描述文件
    @GetMapping
    @RequestMapping("/getMobile")
    public String getMobile(HttpServletRequest request, HttpServletResponse response,@RequestParam String id) throws IOException {
        String keyPath = new File("/sign/mode/cert/key.key").getAbsolutePath();
        String rootPath = new File("/sign/mode/cert/root.crt").getAbsolutePath();
        String serverPath = new File("/sign/mode/cert/server.crt").getAbsolutePath();;
        //模板
        String moblicPath =new File("/sign/mode/udid.mobileconfig").getAbsolutePath();
        //随机
        Long round = new Date().getTime();
        //未签名
        String moblicNoSignPath = new File("/sign/mode/temp/" + round + "no.mobileconfig").getAbsolutePath();
        String temp = IoHandler.readTxt(moblicPath).replace("#{id}", id);
        IoHandler.writeTxt(moblicNoSignPath, temp);
        //已签名
        String moblicSignPath = new File("/sign/mode/temp/" + round + ".mobileconfig").getAbsolutePath();
        String cmd =" openssl smime -sign -in " + moblicNoSignPath + " -out " + moblicSignPath + " -signer " + serverPath + " -inkey " + keyPath + " -certfile " + rootPath + " -outform der -nodetach ";
        RuntimeExec.runtimeExec(cmd);
        return "redirect:../" +  round + ".mobileconfig";
    }


    //301回调
    @PutMapping
    @RequestMapping("/getUdid")
    public void getUdid(int id,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String  redirect = distrbuteService.getUuid(id, request, response);
        response.setHeader("Location", "itms-services://?action=download-manifest&url=https://sign.wlznsb.cn/iosign/" + redirect);

        response.setStatus(301);
    }

    //上传ipa
    @PutMapping
    @RequestMapping(value = "/uploadIpa",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadIpa(@RequestParam MultipartFile ipa, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        int id = distrbuteService.uploadIpa(ipa, request);
        map.put("code", 0);
        map.put("message", "上传成功");
        return map;
    }



}

