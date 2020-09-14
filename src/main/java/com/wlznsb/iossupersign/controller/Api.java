package com.wlznsb.iossupersign.controller;

import com.wlznsb.iossupersign.service.AppleIisServiceImpl;
import com.wlznsb.iossupersign.util.AppleApiUtil;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@Validated
@Slf4j
public class Api {


    @Autowired
    private AppleIisServiceImpl appleIisService;

    @RequestMapping(value = "/addIis",method = RequestMethod.POST)
    public Map<String,Object> addIis(@RequestParam @NotEmpty String iis, @RequestParam @NotEmpty String kid,@RequestParam  MultipartFile p8, HttpServletRequest request) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        String time = String.valueOf(new Date().getTime());
        new File("/sign/mode/temp/" + time).mkdirs();
        //p8路径
        String p8Path = new File("/sign/mode/temp/" + time + "/" + "p.p8").getAbsolutePath();
        log.info("p8路径:" + new File(p8Path).getAbsoluteFile());
        //key路径
        String keyPath = new File("/sign/mode/my.key").getAbsolutePath();
        log.info("key路径:" + keyPath);

        //写入p8这里的new file必须是绝对路径抽象路径无效
        p8.transferTo(new File(p8Path));
        //创建苹果api工具类
        AppleApiUtil appleApiUtil = new AppleApiUtil(iis, kid, p8Path);
        //获取证书工作目录
        String directoryPath = new File("/sign/mode/temp/" + time + "/").getAbsolutePath();
        log.info("证书工作目录:" + directoryPath);
        if(appleApiUtil.init()){
            //删除所有证书
            appleApiUtil.deleCertAll();
            //生成p12
            Map<String,String> map1 =  appleApiUtil.createCert(directoryPath,keyPath,"123456");
            String p12 = map1.get("p12");
            System.out.println(p12);
            map.put("code", 0);
            map.put("message", "申请成功");
            map.put("url", ServerUtil.getRootUrl(request) + time + "/p12.p12");
        }else {
            map.put("code", 1);
            map.put("message", "证书信息不正确");
        }

        return map;
    }




}
