package com.wlznsb.iossupersign.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlznsb.iossupersign.dao.PackStatusIosApkDao;
import com.wlznsb.iossupersign.dao.UserDao;
import com.wlznsb.iossupersign.entity.PackStatusIosApk;
import com.wlznsb.iossupersign.entity.User;
import com.wlznsb.iossupersign.util.IoHandler;
import com.wlznsb.iossupersign.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/pack")
@Validated
@Slf4j
@CrossOrigin(allowCredentials="true")
public class PackIosApkController {

    @Autowired
    private PackStatusIosApkDao packStatusIosApkDao;

    @Autowired
    private UserDao userDao;

    //提交打包信息
    @ResponseBody
    @RequestMapping(value = "/submit" ,method = RequestMethod.POST)
    public Map<String, Object> submit(HttpServletRequest request
            , String statusJson, MultipartFile icon, MultipartFile startIcon) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = (User) request.getSession().getAttribute("user");
            //将json序列化为对象
            PackStatusIosApk packStatusIosApk =  mapper.readValue(statusJson, PackStatusIosApk.class);
            //图标路径
            String iconFile = new File("/sign/mode/temp/" + ServerUtil.getUuid() + ".png").getAbsolutePath();
            //拿应用图标的base64
            String base64Icon = Base64.getEncoder().encodeToString(icon.getBytes());
            //写出
            icon.transferTo(new File(iconFile));
            //判断启动图是否存在
            String iconStartFile;
            if(null != startIcon && startIcon.getSize() != 0){
                //获取启动图最终写出路径
                iconStartFile = new File("/sign/mode/temp/" + ServerUtil.getUuid() + ".png").getAbsolutePath();;
                //写出图片
                startIcon.transferTo(new File(iconStartFile));
            }else {
                iconStartFile = null;
            }
            //定义demo.txt路径
            String demoPath = new File("/sign/mode/static/mode/ios/demo.txt").getAbsolutePath();
            //写出moblie文件路径
            String mobilePath = new File("/sign/mode/temp/" + ServerUtil.getUuid() + ".mobileconfig").getAbsolutePath();
            //获取demo资源并进行替换
            String demo = IoHandler.readTxt(demoPath);
            //替换demo
            demo = demo.replace("标签", packStatusIosApk.getAppName());
            demo =demo.replace("网址", packStatusIosApk.getUrl());
            demo =demo.replace("名称", packStatusIosApk.getName());
            demo =demo.replace("机构", packStatusIosApk.getOrganization());
            demo =demo.replace("描述", packStatusIosApk.getDescribe());
            demo =demo.replace("同意信息", packStatusIosApk.getConsentMessage());
            demo =demo.replace("图片", base64Icon);
            demo =demo.replace("uuid", packStatusIosApk.getPageName());
            demo =demo.replace("标识符", packStatusIosApk.getPageName());
            if(packStatusIosApk.getIsRemove() == 1){
                demo =demo.replace("remove","true");
            }else {
                demo =demo.replace("remove","false");
            }
            //写出未签名moblie文件
            IoHandler.writeTxt(mobilePath, demo);
            //现在用来当mobile路径
            packStatusIosApk.setRemark(mobilePath);
            //判断证书
            if(!("".equals(packStatusIosApk.getServerCert()) |
                    "".equals(packStatusIosApk.getRootCert()) |
                    "".equals(packStatusIosApk.getKeyCert()))){
                String serverCrt = new File("/sign/mode/temp/" + ServerUtil.getUuid() + "server.crt").getAbsolutePath();
                String rootCrt =  new File("/sign/mode/temp/" + ServerUtil.getUuid() + "root.crt").getAbsolutePath();
                String key = new File("/sign/mode/temp/" + ServerUtil.getUuid() + "key.key").getAbsolutePath();
                IoHandler.writeTxt(serverCrt, packStatusIosApk.getServerCert());
                IoHandler.writeTxt(rootCrt,packStatusIosApk.getRootCert());
                IoHandler.writeTxt(key, packStatusIosApk.getKeyCert());
                packStatusIosApk.setServerCert(serverCrt);
                packStatusIosApk.setRootCert(rootCrt);
                packStatusIosApk.setKeyCert(key);
            }else {
                packStatusIosApk.setServerCert(null);
                packStatusIosApk.setRootCert(null);
                packStatusIosApk.setKeyCert(null);
            }
            //写入实体类
            packStatusIosApk.setAccount(user.getAccount());
            packStatusIosApk.setCreateTime(new Date());
            packStatusIosApk.setStatus("排队中");
            packStatusIosApk.setIcon(iconFile);
            packStatusIosApk.setStartIcon(iconStartFile);
            Integer id =  packStatusIosApkDao.submit(packStatusIosApk);
            map.put("code", 0);
            map.put("message", "提交成功");
            map.put("id", id);
        }catch (Exception e){
            log.info(e.toString());
            e.printStackTrace();
            map.put("code", 1);
            map.put("message", "提交失败");
        }
        return map;
    }

}
